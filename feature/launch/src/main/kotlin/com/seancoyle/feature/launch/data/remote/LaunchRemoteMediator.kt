package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.LaunchLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchRemoteDataSource
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val TAG = "LaunchRemoteMediator"
private const val STARTING_PAGE = 0
private const val CACHE_TIMEOUT_HOURS = 1L

@OptIn(ExperimentalPagingApi::class)
internal class LaunchRemoteMediator(
    private val launchRemoteDataSource: LaunchRemoteDataSource,
    private val launchLocalDataSource: LaunchLocalDataSource,
    private val launchQuery: LaunchQuery
) : RemoteMediator<Int, LaunchEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(
            CACHE_TIMEOUT_HOURS,
            TimeUnit.HOURS
        )
        val remoteKeys = launchLocalDataSource.getRemoteKeys()
        val firstKey = remoteKeys.firstOrNull()
        val createdTime = firstKey?.createdAt

        // Check if the query parameters have changed from what was cached
        val cachedQuery = firstKey?.cachedQuery
        val cachedOrder = firstKey?.cachedOrder
        val currentQuery = launchQuery.query
        val currentOrder = launchQuery.order.name

        val queryHasChanged = cachedQuery != currentQuery || cachedOrder != currentOrder

        Timber.tag(TAG).d(
            "Initialize - Current: query='$currentQuery', order=$currentOrder | " +
            "Cached: query='$cachedQuery', order=$cachedOrder | Changed: $queryHasChanged"
        )

        // If query parameters have changed, refresh to get relevant results
        if (queryHasChanged) {
            Timber.tag(TAG).d("Query/order changed - refreshing data.")
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
        state: PagingState<Int, LaunchEntity>
    ): MediatorResult {
        return try {

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = remoteKeyClosestToCurrentPosition(state)
                    remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem()
                    val prevKey = remoteKey?.prevKey
                    Timber.tag(TAG).d("LoadType.PREPEND - prev page: $prevKey")
                    // If prevKey is null, we've reached the beginning
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem()
                    // If remoteKey is null, that means refresh has never been called
                    // If nextKey is null, that means we've reached the end
                    val nextKey = remoteKey?.nextKey
                    Timber.tag(TAG).d("LoadType.APPEND - next page: $nextKey")
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
            }

            Timber.tag(TAG).d("API call with $page and $launchQuery")
            when (val remoteLaunchesResult = launchRemoteDataSource.getLaunches(page, launchQuery)) {
                is LaunchResult.Success -> {
                    val launches = remoteLaunchesResult.data
                    val endOfPaginationReached = launches.size < state.config.pageSize
                    val nextPage = if (endOfPaginationReached) null else page.plus(1)
                    val prevPage = if (page > 0) page.minus(1) else null

                    Timber.tag(TAG).d(
                        "Loaded ${launches.size} items for page $page. " +
                                "EndReached: $endOfPaginationReached, NextPage: $nextPage, PrevPage: $prevPage"
                    )

                    // Save to database with remote keys in a transaction
                    if (loadType == LoadType.REFRESH) {
                        // Clear all data and insert fresh data
                        // When refreshing, we always start from page 0, so prevPage should be null
                        Timber.tag(TAG).d("REFRESH - refreshing cache with new data")
                        launchLocalDataSource.refreshLaunchesWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = null, // Always null on refresh since we're starting fresh
                            currentPage = STARTING_PAGE,
                            cachedQuery = launchQuery.query,
                            cachedOrder = launchQuery.order.name
                        )
                    } else {
                        // Append or prepend data to existing cache
                        Timber.tag(TAG).d("$loadType - APPEND - appending data to cache")
                        launchLocalDataSource.appendLaunchesWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = prevPage,
                            currentPage = page,
                            cachedQuery = launchQuery.query,
                            cachedOrder = launchQuery.order.name
                        )
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is LaunchResult.Error -> {
                    Timber.tag(TAG).e("Error loading page $page: ${remoteLaunchesResult.error}")
                    return fallbackToCacheAvailable(loadType, remoteLaunchesResult.error)
                }
            }
        } catch (exception: Exception) {
            Timber.tag(TAG).e(exception, "Exception in load()")
            return fallbackToCacheAvailable(loadType, exception)
        }
    }

    private suspend fun fallbackToCacheAvailable(
        loadType: LoadType,
        exception: Throwable): MediatorResult {
        // Check if we have cached data to fall back to
        val cachedItemCount = when (val totalResult = launchLocalDataSource.getTotalEntries()) {
            is LaunchResult.Success -> totalResult.data
            is LaunchResult.Error -> 0
        }

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

    private suspend fun getRemoteKeyForFirstItem(): LaunchRemoteKeyEntity? {
        return launchLocalDataSource.getRemoteKeys().firstOrNull()
    }

    private suspend fun getRemoteKeyForLastItem(): LaunchRemoteKeyEntity? {
        return launchLocalDataSource.getRemoteKeys().lastOrNull()
    }

    private suspend fun remoteKeyClosestToCurrentPosition(
        state: PagingState<Int, LaunchEntity>
    ): LaunchRemoteKeyEntity? {
        val position = state.anchorPosition ?: return null
        val item = state.closestItemToPosition(position) ?: return null
        return launchLocalDataSource.getRemoteKey(item.id)
    }

}
