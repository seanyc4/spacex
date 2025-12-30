package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.LaunchConstants
import com.seancoyle.feature.launch.data.repository.LaunchRemoteDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.model.LaunchType
import timber.log.Timber
import javax.inject.Inject

internal class LaunchRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val crashlytics: Crashlytics
) : LaunchRemoteDataSource {

    override suspend fun getLaunches(
        page: Int,
        launchQuery: LaunchQuery
    ): LaunchResult<List<Launch>, Throwable> {
        return runSuspendCatching {
            val result = when (launchQuery.launchType) {
                LaunchType.UPCOMING -> api.getUpcomingLaunches(
                    offset = page * LaunchConstants.PAGINATION_LIMIT,
                    search = launchQuery.query
                )
                LaunchType.PAST -> api.getPreviousLaunches(
                    offset = page * LaunchConstants.PAGINATION_LIMIT,
                    search = launchQuery.query
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
}
