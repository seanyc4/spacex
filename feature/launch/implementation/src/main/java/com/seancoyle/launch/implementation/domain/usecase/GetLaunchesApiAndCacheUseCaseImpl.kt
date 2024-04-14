package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.network.LaunchNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val insertLaunchesToCacheUseCase: InsertLaunchesToCacheUseCase,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(): Flow<DataResult<List<LaunchTypes.Launch>, DataError>> = flow {
        emit(getLaunchesFromNetwork())
    }

    private suspend fun getLaunchesFromNetwork(): DataResult<List<LaunchTypes.Launch>, DataError> {
        return when (val networkResult = launchNetworkDataSource.getLaunches(launchOptions)) {
            is DataResult.Success -> cacheData(networkResult.data)
            is DataResult.Error -> DataResult.Error(networkResult.error)
        }
    }

    private suspend fun cacheData(launches: List<LaunchTypes.Launch>): DataResult<List<LaunchTypes.Launch>, DataError> {
        return when (val cacheResult = insertLaunchesToCacheUseCase(launches)) {
            is DataResult.Success -> DataResult.Success(launches)
            is DataResult.Error -> DataResult.Error(cacheResult.error)
        }
    }
}