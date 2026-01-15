package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery

private const val TAG = "UpcomingRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
internal class UpcomingLaunchesRemoteMediator(
    remoteDataSource: LaunchesRemoteDataSource,
    launchesLocalDataSource: LaunchesLocalDataSource<UpcomingRemoteKeyEntity>,
    detailLocalDataSource: DetailLocalDataSource,
    launchesQuery: LaunchesQuery
) : LaunchesRemoteMediator<UpcomingLaunchEntity, UpcomingRemoteKeyEntity>(
    tag = TAG,
    launchesLocalDataSource = launchesLocalDataSource,
    detailLocalDataSource = detailLocalDataSource,
    launchesQuery = launchesQuery,
    entityId = { it.id },
    remoteKeyCreatedAt = { it.createdAt },
    remoteKeyCachedQuery = { it.cachedQuery },
    remoteKeyCachedLaunchStatus = { it.cachedLaunchStatus },
    remoteKeyPrevKey = { it.prevKey },
    remoteKeyNextKey = { it.nextKey },
    fetchPage = { page, query ->
        val result: LaunchResult<RemoteDetailedLaunches, Throwable> =
            when (val res = remoteDataSource.getUpcomingDetailedLaunches(page, query)) {
                is LaunchResult.Success -> LaunchResult.Success(
                    RemoteDetailedLaunches(
                        summaries = res.data.summaries,
                        details = res.data.details
                    )
                )

                is LaunchResult.Error -> LaunchResult.Error(res.error)
            }
        result
    }
)
