package com.seancoyle.feature.launch.implementation.data.remote

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRemoteDataSource
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import timber.log.Timber
import javax.inject.Inject

internal class LaunchRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val crashlytics: Crashlytics
) : LaunchRemoteDataSource {

    override suspend fun getLaunches(
        page: Int,
        launchQuery: LaunchQuery
    ): LaunchResult<List<LaunchTypes.Launch>, Throwable> {
        return runSuspendCatching {
            val result = api.getUpcomingLaunches(
                offset = page * PAGINATION_LIMIT,
                search = launchQuery.query,
                ordering = launchQuery.order.value
            )
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
