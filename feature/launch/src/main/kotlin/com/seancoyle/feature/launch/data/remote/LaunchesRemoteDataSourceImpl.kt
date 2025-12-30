package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.presentation.launches.LaunchesConstants
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.model.LaunchesType
import timber.log.Timber
import javax.inject.Inject

internal class LaunchesRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val crashlytics: Crashlytics
) : LaunchesRemoteDataSource {

    override suspend fun getLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<List<Launch>, Throwable> {
        return runSuspendCatching {
            val result = when (launchesQuery.launchesType) {
                LaunchesType.UPCOMING -> api.getUpcomingLaunches(
                    offset = page * LaunchesConstants.PAGINATION_LIMIT,
                    search = launchesQuery.query
                )
                LaunchesType.PAST -> api.getPreviousLaunches(
                    offset = page * LaunchesConstants.PAGINATION_LIMIT,
                    search = launchesQuery.query
                )
            }
            result.toDomain()
        }.fold(
            onSuccess = { mappedResult ->
                LaunchResult.Success(mappedResult)
            },
            onFailure = { exception ->
                Timber.e(exception)
                crashlytics.logException(exception)
                LaunchResult.Error(exception)
            }
        )
    }

    override suspend fun getLaunch(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<List<Launch>, RemoteError>{
        return runSuspendCatching {
            val result = when (launchType) {
                LaunchesType.UPCOMING -> api.getUpcomingLaunch(id)
                LaunchesType.PAST -> api.getPreviousLaunch(id)
            }
            result.toDomain()
        }.fold(
            onSuccess = { mappedResult ->
                LaunchResult.Success(mappedResult)
            },
            onFailure = { exception ->
                Timber.e(exception)
                crashlytics.logException(exception)
                val mappedError = map(exception)
                LaunchResult.Error(mappedError)
            }
        )
    }
}
