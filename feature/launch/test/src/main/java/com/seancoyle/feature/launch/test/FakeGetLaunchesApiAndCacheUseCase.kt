package com.seancoyle.feature.launch.test

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetLaunchesApiAndCacheUseCase : GetLaunchesApiAndCacheUseCase {

    val isSuccess = true
    override fun invoke(): Flow<LaunchResult<Unit, DataSourceError>> = flow {
        emit(getFakeLaunchesFromNetwork())
    }

    private fun getFakeLaunchesFromNetwork(): LaunchResult<Unit, DataSourceError> {
        // Simulating network data fetching behavior
        return if (shouldSimulateSuccess(isSuccess)) {
            // Simulate a successful data fetch
            LaunchResult.Success(Unit)
        } else {
            // Simulate a network error
            LaunchResult.Error(DataSourceError.NETWORK_CONNECTION_FAILED)
        }
    }

    private fun shouldSimulateSuccess(isSuccess: Boolean): Boolean {
        return isSuccess
    }
}