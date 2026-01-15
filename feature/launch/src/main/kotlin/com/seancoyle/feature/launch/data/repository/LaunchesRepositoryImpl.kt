package com.seancoyle.feature.launch.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.data.local.toDomain
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
    private val upcomingPagerFactory: UpcomingLaunchesPagerFactory,
    private val pastPagerFactory: PastLaunchesPagerFactory,
    private val launchesRemoteDataSource: LaunchesRemoteDataSource,
    private val upcomingDetailLocalDataSource: UpcomingDetailLocalDataSource,
    private val pastDetailLocalDataSource: PastDetailLocalDataSource,
) : LaunchesRepository {

    override fun upcomingPager(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        Timber.tag(TAG).d("Creating UPCOMING pager with query: $launchesQuery")
        return upcomingPagerFactory.create(launchesQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

    override fun pastPager(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        Timber.tag(TAG).d("Creating PAST pager with query: $launchesQuery")
        return pastPagerFactory.create(launchesQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getLaunch(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, RemoteError> {
        val cachedLaunch: Launch? = when (launchType) {
            LaunchesType.UPCOMING -> {
                when (val cachedResult = upcomingDetailLocalDataSource.getLaunchDetail(id)) {
                    is LaunchResult.Success -> cachedResult.data
                    is LaunchResult.Error -> null
                }
            }

            LaunchesType.PAST -> {
                when (val cachedResult = pastDetailLocalDataSource.getLaunchDetail(id)) {
                    is LaunchResult.Success -> cachedResult.data
                    is LaunchResult.Error -> null
                }
            }
        }

        if (cachedLaunch != null) {
            Timber.tag(TAG).d("getLaunch: Cache hit for launch id=$id type=$launchType")
            return LaunchResult.Success(cachedLaunch)
        }

        Timber.tag(TAG).d("getLaunch: Cache miss for launch id=$id type=$launchType, fetching from network")
        return launchesRemoteDataSource.getLaunch(
            id = id,
            launchType = launchType
        )
    }
}
