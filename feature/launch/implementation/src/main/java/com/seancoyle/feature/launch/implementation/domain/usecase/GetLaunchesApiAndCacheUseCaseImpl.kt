package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val launchRepository: LaunchRepository,
    private val launchOptions: LaunchOptions
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(): Flow<Result<Unit, DataError>> = flow {
        emit(getLaunchesFromNetwork())
    }

    private suspend fun getLaunchesFromNetwork(): Result<Unit, DataError> {
        return when (val networkResult = launchRepository.getLaunchesAndCache(launchOptions)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(networkResult.error)
        }
    }
}