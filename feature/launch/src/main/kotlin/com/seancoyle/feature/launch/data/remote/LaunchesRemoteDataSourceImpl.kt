package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants
import timber.log.Timber
import javax.inject.Inject

internal class LaunchesRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val crashlytics: Crashlytics
) : LaunchesRemoteDataSource {

    override suspend fun getLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<List<LaunchSummary>, Throwable> {
        return runSuspendCatching {
            val result = when (launchesQuery.launchesType) {
                LaunchesType.UPCOMING -> api.getUpcomingLaunches(
                    offset = page * LaunchesConstants.PAGINATION_LIMIT,
                    search = launchesQuery.query,
                    status = launchesQuery.status?.id
                )

                LaunchesType.PAST -> api.getPreviousLaunches(
                    offset = page * LaunchesConstants.PAGINATION_LIMIT,
                    search = launchesQuery.query,
                    status = launchesQuery.status?.id
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
    ): LaunchResult<Launch, RemoteError> {
        return runSuspendCatching {
            val result = when (launchType) {
                LaunchesType.UPCOMING -> api.getUpcomingLaunch(id)
                LaunchesType.PAST -> api.getPreviousLaunch(id)
            }
            result.toDomain()
        }.fold(
            onSuccess = { mappedResult ->
                if (mappedResult != null) {
                    LaunchResult.Success(mappedResult)
                } else {
                    LaunchResult.Error(RemoteError.NETWORK_DATA_NULL)
                }
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
