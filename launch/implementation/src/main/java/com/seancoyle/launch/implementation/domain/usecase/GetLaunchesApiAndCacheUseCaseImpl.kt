package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Launch
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

    override operator fun invoke(): Flow<DataResult<List<Launch>, DataError>> = flow {
        emit(getLaunchesFromNetwork())
    }

    private suspend fun getLaunchesFromNetwork(): DataResult<List<Launch>, DataError> {
        return when (val networkResult = launchNetworkDataSource.getLaunches(launchOptions)) {
            is DataResult.Success -> cacheData(networkResult.data)
            is DataResult.Error -> DataResult.Error(networkResult.error)
            is DataResult.Loading -> DataResult.Loading
        }
    }

    private suspend fun cacheData(launches: List<Launch>): DataResult<List<Launch>, DataError> {
        return when (val cacheResult = insertLaunchesToCacheUseCase(launches)) {
            is DataResult.Success -> DataResult.Success(launches)
            is DataResult.Error -> DataResult.Error(cacheResult.error)
            is DataResult.Loading -> DataResult.Loading
        }
    }
}