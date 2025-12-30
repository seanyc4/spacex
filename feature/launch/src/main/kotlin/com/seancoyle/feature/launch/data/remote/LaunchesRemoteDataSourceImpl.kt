package com.seancoyle.feature.launch.data.remote

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.LaunchesConstants
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
}
