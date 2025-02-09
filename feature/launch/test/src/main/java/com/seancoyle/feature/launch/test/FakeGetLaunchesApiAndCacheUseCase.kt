package com.seancoyle.feature.launch.test

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetLaunchesApiAndCacheUseCase : GetLaunchesApiAndCacheUseCase {

    val isSuccess = true
    override fun invoke(): Flow<Result<Unit, DataError>> = flow {
        emit(getFakeLaunchesFromNetwork())
    }

    private fun getFakeLaunchesFromNetwork(): Result<Unit, DataError> {
        // Simulating network data fetching behavior
        return if (shouldSimulateSuccess(isSuccess)) {
            // Simulate a successful data fetch
            Result.Success(Unit)
        } else {
            // Simulate a network error
            Result.Error(DataError.NETWORK_CONNECTION_FAILED)
        }
    }

    private fun shouldSimulateSuccess(isSuccess: Boolean): Boolean {
        return isSuccess
    }
}