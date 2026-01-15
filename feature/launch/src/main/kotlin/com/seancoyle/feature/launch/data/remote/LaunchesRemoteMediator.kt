package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val STARTING_PAGE = 0
private const val CACHE_TIMEOUT_HOURS = 1L

@OptIn(ExperimentalPagingApi::class)
internal open class LaunchesRemoteMediator<Entity : Any, RemoteKey : Any>(
    private val tag: String,
    private val launchesLocalDataSource: LaunchesLocalDataSource<RemoteKey>,
    private val detailLocalDataSource: DetailLocalDataSource,
    private val launchesQuery: LaunchesQuery,
    private val entityId: (Entity) -> String,
    private val remoteKeyCreatedAt: (RemoteKey) -> Long?,
    private val remoteKeyCachedQuery: (RemoteKey) -> String?,
    private val remoteKeyCachedLaunchStatus: (RemoteKey) -> String?,
    private val remoteKeyPrevKey: (RemoteKey) -> Int?,
    private val remoteKeyNextKey: (RemoteKey) -> Int?,
    private val fetchPage: suspend (page: Int, query: LaunchesQuery) -> LaunchResult<RemoteDetailedLaunches, Throwable>,
) : RemoteMediator<Int, Entity>() {

    internal data class RemoteDetailedLaunches(
        val summaries: List<LaunchSummary>,
        val details: List<Launch>,
    )

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(CACHE_TIMEOUT_HOURS, TimeUnit.HOURS)

        val remoteKeys = launchesLocalDataSource.getRemoteKeys()
        val firstKey = remoteKeys.firstOrNull()
        val createdTime = firstKey?.let(remoteKeyCreatedAt)

        val cachedQuery = firstKey?.let(remoteKeyCachedQuery)
        val cachedLaunchStatus = firstKey?.let(remoteKeyCachedLaunchStatus)
        val currentQuery = launchesQuery.query
        val currentLaunchStatus = launchesQuery.status?.name

        val queryHasChanged = cachedQuery != currentQuery || cachedLaunchStatus != currentLaunchStatus

        Timber.tag(tag).d(
            "Initialize - Current: query='$currentQuery', launchStatus=$currentLaunchStatus | " +
                "Cached: query='$cachedQuery', launchStatus=$cachedLaunchStatus | Changed: $queryHasChanged"
        )

        if (queryHasChanged) {
            Timber.tag(tag).d("Query/order/launchType changed - refreshing data.")
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

        return if (createdTime != null && System.currentTimeMillis().minus(createdTime) <= cacheTimeout) {
            Timber.tag(tag).d("Skipping initial refresh; cache is still valid.")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Timber.tag(tag).d("Initial Refresh; cache is stale - fetching from network.")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Entity>): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = remoteKeyClosestToCurrentPosition(state)
                    val nextKey = remoteKey?.let(remoteKeyNextKey)
                    nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    val firstKey = launchesLocalDataSource.getRemoteKeys().firstOrNull()
                    val prevKey = firstKey?.let(remoteKeyPrevKey)
                    Timber.tag(tag).d("LoadType.PREPEND - prev page: $prevKey")
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastKey = launchesLocalDataSource.getRemoteKeys().lastOrNull()
                    val nextKey = lastKey?.let(remoteKeyNextKey)
                    Timber.tag(tag).d("LoadType.APPEND - next page: $nextKey")
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = lastKey != null)
                }
            }

            Timber.tag(tag).d("API call with page=$page and query=$launchesQuery")

            return when (val remote = fetchPage(page, launchesQuery)) {
                is LaunchResult.Success -> {
                    val launchDetails = remote.data.details
                    val launches = remote.data.summaries

                    val endOfPaginationReached = launches.isEmpty() || launches.size < state.config.pageSize
                    val nextPage = if (endOfPaginationReached) null else page.plus(1)
                    val prevPage = if (page > 0) page.minus(1) else null

                    Timber.tag(tag).d(
                        "Loaded ${launches.size} items for page $page. " +
                            "EndReached: $endOfPaginationReached, NextPage: $nextPage, PrevPage: $prevPage"
                    )

                    if (loadType == LoadType.REFRESH) {
                        Timber.tag(tag).d("REFRESH - refreshing cache with new data")
                        detailLocalDataSource.refreshLaunches(launchDetails)
                        launchesLocalDataSource.refreshWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = null,
                            currentPage = STARTING_PAGE,
                            cachedQuery = launchesQuery.query,
                            cachedLaunchStatus = launchesQuery.status?.name
                        )
                    } else {
                        Timber.tag(tag).d("$loadType - appending data to cache")
                        detailLocalDataSource.upsertAllLaunchDetails(launchDetails)
                        launchesLocalDataSource.appendWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = prevPage,
                            currentPage = page,
                            cachedQuery = launchesQuery.query,
                            cachedLaunchStatus = launchesQuery.status?.name
                        )
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is LaunchResult.Error -> {
                    Timber.tag(tag).e("Error loading page $page: ${remote.error}")
                    fallbackToCacheAvailable(loadType, Exception("${remote.error}"))
                }
            }
        } catch (e: Exception) {
            Timber.tag(tag).e(e, "Exception in load()")
            return fallbackToCacheAvailable(loadType, e)
        }
    }

    private suspend fun fallbackToCacheAvailable(loadType: LoadType, exception: Throwable): MediatorResult {
        val cachedItemCount = launchesLocalDataSource.getTotalEntries()

        if (loadType == LoadType.REFRESH && cachedItemCount > 0) {
            Timber.tag(tag).d("Exception during REFRESH but have $cachedItemCount cached items, showing cache")
            return MediatorResult.Success(endOfPaginationReached = false)
        }

        Timber.tag(tag).d("Exception during $loadType, returning error for retry")
        return MediatorResult.Error(exception)
    }

    private suspend fun remoteKeyClosestToCurrentPosition(state: PagingState<Int, Entity>): RemoteKey? {
        val position = state.anchorPosition ?: return null
        val item = state.closestItemToPosition(position) ?: return null
        return launchesLocalDataSource.getRemoteKey(entityId(item))
    }
}
