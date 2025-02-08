package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val insertLaunchesToCacheUseCase: InsertLaunchesToCacheUseCase,
    private val launchRepository: LaunchRepository,
    private val launchOptions: LaunchOptions
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(): Flow<Result<List<LaunchTypes.Launch>, DataError>> = flow {
        emit(getLaunchesFromNetwork())
    }

    private suspend fun getLaunchesFromNetwork(): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val networkResult = launchRepository.getLaunches(launchOptions)) {
            is Result.Success -> cacheData(networkResult.data)
            is Result.Error -> Result.Error(networkResult.error)
        }
    }

    private suspend fun cacheData(launches: List<LaunchTypes.Launch>): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val cacheResult = insertLaunchesToCacheUseCase(launches)) {
            is Result.Success -> Result.Success(launches)
            is Result.Error -> Result.Error(cacheResult.error)
        }
    }
}