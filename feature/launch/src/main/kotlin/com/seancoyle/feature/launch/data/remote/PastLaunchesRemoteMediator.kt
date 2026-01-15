package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery

private const val TAG = "PastRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
internal class PastLaunchesRemoteMediator(
    remoteDataSource: LaunchesRemoteDataSource,
    launchesLocalDataSource: LaunchesLocalDataSource<PastRemoteKeyEntity>,
    detailLocalDataSource: DetailLocalDataSource,
    launchesQuery: LaunchesQuery,
) : LaunchesRemoteMediator<PastLaunchEntity, PastRemoteKeyEntity>(
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
        val mapped: LaunchResult<RemoteDetailedLaunches, Throwable> =
            when (val res = remoteDataSource.getPastDetailedLaunches(page, query)) {
                is LaunchResult.Success -> LaunchResult.Success(
                    RemoteDetailedLaunches(
                        summaries = res.data.summaries,
                        details = res.data.details
                    )
                )

                is LaunchResult.Error -> LaunchResult.Error(res.error)
            }
        mapped
    }
)
