package com.seancoyle.feature.launch.implementation.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.REMOTE_KEY_ID
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.implementation.data.repository.LaunchLocalDataSource
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRemoteDataSource
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "LaunchRemoteMediator"
private const val STARTING_PAGE = 0
private const val CACHE_TIMEOUT_HOURS = 1L

@OptIn(ExperimentalPagingApi::class)
internal class LaunchRemoteMediator @Inject constructor(
    private val launchRemoteDataSource: LaunchRemoteDataSource,
    private val launchLocalDataSource: LaunchLocalDataSource
) : RemoteMediator<Int, LaunchEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(CACHE_TIMEOUT_HOURS, TimeUnit.HOURS)
        val createdTime = launchLocalDataSource.getRemoteKeyCreationTime(REMOTE_KEY_ID)
        return if (System.currentTimeMillis().minus(createdTime ?: 0) <= cacheTimeout) {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            Timber.tag(TAG).d("Skipping initial refresh; cache is still valid.")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            Timber.tag(TAG).d("Launching initial refresh; cache is stale.")
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
                    Timber.tag(TAG).d("LoadType.REFRESH - starting from page $STARTING_PAGE")
                    STARTING_PAGE
                }

                LoadType.PREPEND -> {
                    Timber.tag(TAG).d("LoadType.PREPEND called - end of pagination reached")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    // Get the last remote key to determine next page
                    val remoteKey = launchLocalDataSource.getRemoteKey(REMOTE_KEY_ID)
                    val nextPage = remoteKey?.nextKey

                    if (nextPage == null) {
                        Timber.tag(TAG)
                            .d("LoadType.APPEND - no next page, end of pagination reached")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    Timber.tag(TAG).d("LoadType.APPEND - loading page $nextPage")
                    nextPage
                }
            }

            // Fetch data from the network
            when (val remoteLaunchesResult = launchRemoteDataSource.getLaunches(page)) {
                is LaunchResult.Success -> {
                    val launches = remoteLaunchesResult.data
                    val endOfPaginationReached = launches.isEmpty()

                    // Calculate the next page (null if we've reached the end)
                    val nextPage = if (endOfPaginationReached) {
                        null
                    } else {
                        page + 1
                    }

                    Timber.tag(TAG).d(
                        "Loaded ${launches.size} items for page $page. " +
                                "EndReached: $endOfPaginationReached, NextPage: $nextPage"
                    )

                    // Save to database with remote keys in a transaction
                    if (loadType == LoadType.REFRESH) {
                        // Clear all data and insert fresh data
                        launchLocalDataSource.refreshLaunchesWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = null,
                            currentPage = STARTING_PAGE

                        )
                    } else {
                        // Append data to existing cache
                        launchLocalDataSource.appendLaunchesWithKeys(
                            launches = launches,
                            nextPage = nextPage,
                            prevPage = page.minus(1),
                            currentPage = page
                        )
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is LaunchResult.Error -> {
                    Timber.tag(TAG).e("Error loading page $page: ${remoteLaunchesResult.error}")
                    fallbackToCacheAvailable(loadType)
                    MediatorResult.Error(remoteLaunchesResult.error)
                }
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Exception in load()")
            fallbackToCacheAvailable(loadType)
            MediatorResult.Error(e)
        }
    }

    private suspend fun fallbackToCacheAvailable(loadType: LoadType): MediatorResult {
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

        // If we're appending and hit an exception, we've reached the end for now
        if (loadType == LoadType.APPEND) {
            Timber.tag(TAG).d("Exception during APPEND, ending pagination")
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        return MediatorResult.Error(Exception("No cached data available"))
    }
}
