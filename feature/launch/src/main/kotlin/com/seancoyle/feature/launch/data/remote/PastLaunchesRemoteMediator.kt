package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val TAG = "PastRemoteMediator"
private const val STARTING_PAGE = 0
private const val CACHE_TIMEOUT_HOURS = 1L

@OptIn(ExperimentalPagingApi::class)
internal class PastLaunchesRemoteMediator(
    private val remoteDataSource: LaunchesRemoteDataSource,
    private val pastLaunchesLocalDataSource: LaunchesLocalDataSource<PastRemoteKeyEntity>,
    private val pastDetailLocalDataSource: DetailLocalDataSource,
    private val launchesQuery: LaunchesQuery,
) : RemoteMediator<Int, PastLaunchEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(
            CACHE_TIMEOUT_HOURS,
            TimeUnit.HOURS
        )
        val remoteKeys = pastLaunchesLocalDataSource.getRemoteKeys()
        val createdTime = remoteKeys.firstOrNull()?.createdAt

        // Check if the query parameters have changed from what was cached
        val cachedQuery = remoteKeys.firstOrNull()?.cachedQuery
        val cachedLaunchStatus = remoteKeys.firstOrNull()?.cachedLaunchStatus
        val currentQuery = launchesQuery.query
        val currentLaunchStatus = launchesQuery.status?.name

        val queryHasChanged = cachedQuery != currentQuery
                || cachedLaunchStatus != currentLaunchStatus

        Timber.tag(TAG).d(
            "Initialize - Current: query='$currentQuery', launchStatus=$currentLaunchStatus | " +
                    "Cached: query='$cachedQuery', launchStatus=$cachedLaunchStatus | Changed: $queryHasChanged"
        )

        // If query parameters have changed, refresh to get relevant results
        if (queryHasChanged) {
            Timber.tag(TAG).d("Query/order/launchType changed - refreshing data.")
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

        // Check if the cache is still valid based on createdAt timestamp & cache timeout
        return if (createdTime != null &&
            System.currentTimeMillis().minus(createdTime) <= cacheTimeout
        ) {
            Timber.tag(TAG).d("Skipping initial refresh; cache is still valid.")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Timber.tag(TAG).d("Initial Refresh; cache is stale - fetching from network.")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PastLaunchEntity>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = remoteKeyClosestToCurrentPosition(state)
                    val nextKey = remoteKey?.nextKey
                    nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    val remoteKeys = pastLaunchesLocalDataSource.getRemoteKeys()
                    val firstKey = remoteKeys.firstOrNull()
                    val prevKey = firstKey?.prevKey
                    Timber.tag(TAG).d("LoadType.PREPEND - prev page: $prevKey")
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKeys = pastLaunchesLocalDataSource.getRemoteKeys()
                    val lastKey = remoteKeys.lastOrNull()
                    val nextKey = lastKey?.nextKey
                    Timber.tag(TAG).d("LoadType.APPEND - next page: $nextKey")
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = lastKey != null)
                }
            }

            Timber.tag(TAG).d("API call with page=$page and query=$launchesQuery")
            when (val remoteLaunchesResult = remoteDataSource.getPastDetailedLaunches(page, launchesQuery)) {
                is LaunchResult.Success -> {
                    val launchDetails = remoteLaunchesResult.data.details
                    val launches = remoteLaunchesResult.data.summaries
                    val endOfPaginationReached = launches.isEmpty() || launches.size < state.config.pageSize
                    val nextPage = if (endOfPaginationReached) null else page.plus(1)
                    val prevPage = if (page > 0) page.minus(1) else null

                    Timber.tag(TAG).d(
                        "Loaded ${launches.size} items for page $page. " +
                                "EndReached: $endOfPaginationReached, NextPage: $nextPage, PrevPage: $prevPage"
                    )

                    if (loadType == LoadType.REFRESH) {
                        Timber.tag(TAG).d("REFRESH - refreshing cache with new data")
                        pastDetailLocalDataSource.refreshLaunches(launchDetails)
                        pastLaunchesLocalDataSource.refreshWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = null, // Always null on refresh since we're starting fresh
                            currentPage = STARTING_PAGE,
                            cachedQuery = launchesQuery.query,
                            cachedLaunchStatus = launchesQuery.status?.name
                        )
                    } else {
                        Timber.tag(TAG).d("$loadType - appending data to cache")
                        pastDetailLocalDataSource.upsertAllLaunchDetails(launchDetails)
                        pastLaunchesLocalDataSource.appendWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = prevPage,
                            currentPage = page,
                            cachedQuery = launchesQuery.query,
                            cachedLaunchStatus = launchesQuery.status?.name
                        )
                    }

                    return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is LaunchResult.Error -> {
                    Timber.tag(TAG).e("Error loading page $page: ${remoteLaunchesResult.error}")
                    return fallbackToCacheAvailable(loadType, Exception("${remoteLaunchesResult.error}"))
                }
            }
        } catch (exception: Exception) {
            Timber.tag(TAG).e(exception, "Exception in load()")
            return fallbackToCacheAvailable(loadType, exception)
        }
    }

    private suspend fun fallbackToCacheAvailable(
        loadType: LoadType,
        exception: Throwable
    ): MediatorResult {
        val cachedItemCount = pastLaunchesLocalDataSource.getTotalEntries()

        // If we're refreshing and have cached data, allow it to be displayed
        if (loadType == LoadType.REFRESH && cachedItemCount > 0) {
            Timber.tag(TAG)
                .d("Exception during REFRESH but have $cachedItemCount cached items, showing cache")
            return MediatorResult.Success(endOfPaginationReached = false)
        }

        // For APPEND and PREPEND, return error so retry button can be shown
        Timber.tag(TAG).d("Exception during $loadType, returning error for retry")
        return MediatorResult.Error(exception)
    }

    private suspend fun remoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PastLaunchEntity>
    ): PastRemoteKeyEntity? {
        val position = state.anchorPosition ?: return null
        val item = state.closestItemToPosition(position) ?: return null
        return pastLaunchesLocalDataSource.getRemoteKey(item.id)
    }
}
