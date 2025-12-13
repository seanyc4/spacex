package com.seancoyle.feature.launch.implementation.data.remote

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRemoteDataSource
import timber.log.Timber
import javax.inject.Inject

internal class LaunchRemoteDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val crashlytics: Crashlytics
) : LaunchRemoteDataSource {

    override suspend fun getLaunches(offset: Int): LaunchResult<List<LaunchTypes.Launch>, RemoteError> {
        return runSuspendCatching {
            val result = api.getUpcomingLaunches(offset)
            result.toDomain()
        }.fold(
            onSuccess = { mappedResult ->
                LaunchResult.Success(mappedResult)
            },
            onFailure = { exception ->
                Timber.e(exception)
                crashlytics.logException(exception)
                LaunchResult.Error(map(exception))
            }
        )
    }
}
