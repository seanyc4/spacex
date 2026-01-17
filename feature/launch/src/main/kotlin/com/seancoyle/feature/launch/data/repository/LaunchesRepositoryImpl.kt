package com.seancoyle.feature.launch.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.data.local.toDomain
import com.seancoyle.feature.launch.di.PastLaunches
import com.seancoyle.feature.launch.di.UpcomingLaunches
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "LaunchesRepositoryImpl"

internal class LaunchesRepositoryImpl @Inject constructor(
    private val pagerFactory: LaunchesPagerFactory,
    private val launchesRemoteDataSource: LaunchesRemoteDataSource,
    @param:UpcomingLaunches private val upcomingDetailLocalDataSource: DetailLocalDataSource,
    @param:PastLaunches private val pastDetailLocalDataSource: DetailLocalDataSource,
) : LaunchesRepository {

    override fun upcomingPager(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        Timber.tag(TAG).d("Creating UPCOMING pager with query: $launchesQuery")
        return pagerFactory.createUpcoming(launchesQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

    override fun pastPager(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        Timber.tag(TAG).d("Creating PAST pager with query: $launchesQuery")
        return pagerFactory.createPast(launchesQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getLaunch(
        id: String,
        launchType: LaunchesType,
        isRefresh: Boolean
    ): LaunchResult<Launch, RemoteError> {
        if (isRefresh) {
            Timber.tag(TAG).d("getLaunch: Refresh requested for launch id=$id type=$launchType, fetching from network")
            return fetchLaunchFromNetwork(
                id = id,
                launchType = launchType
            )
        }

        cachedLaunchOrNull(id = id, launchType = launchType)?.let { cached ->
            Timber.tag(TAG).d("getLaunch: Cache hit for launch id=$id type=$launchType")
            return LaunchResult.Success(cached)
        }

        Timber.tag(TAG).d("getLaunch: Cache miss for launch id=$id type=$launchType, fetching from network")
        return fetchLaunchFromNetwork(
            id = id,
            launchType = launchType
        )
    }

    private fun detailLocalDataSourceFor(launchType: LaunchesType): DetailLocalDataSource = when (launchType) {
        LaunchesType.UPCOMING -> upcomingDetailLocalDataSource
        LaunchesType.PAST -> pastDetailLocalDataSource
    }

    private suspend fun cachedLaunchOrNull(id: String, launchType: LaunchesType): Launch? {
        return when (val cachedResult = detailLocalDataSourceFor(launchType).getLaunchDetail(id)) {
            is LaunchResult.Success -> cachedResult.data
            is LaunchResult.Error -> null
        }
    }

    private suspend fun fetchLaunchFromNetwork(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, RemoteError> {
        return launchesRemoteDataSource.getLaunch(
            id = id,
            launchType = launchType
        )
    }
}
