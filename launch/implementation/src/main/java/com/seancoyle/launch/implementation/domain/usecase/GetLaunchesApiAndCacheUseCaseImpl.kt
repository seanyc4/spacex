package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.NetworkErrors.UNKNOWN_NETWORK_ERROR
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val insertLaunchesToCacheUseCase: InsertLaunchesToCacheUseCase,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(): Flow<DataResult<List<Launch>>> = flow {
        emit(getLaunchesFromNetwork())
    }

    private suspend fun getLaunchesFromNetwork(): DataResult<List<Launch>> {
        return when (val networkResult = launchNetworkDataSource.getLaunches(launchOptions)) {
            is DataResult.Success -> cacheData(networkResult.data)
            is DataResult.Error -> DataResult.Error(networkResult.exception)
            else -> DataResult.Error(UNKNOWN_NETWORK_ERROR)
        }
    }

    private suspend fun cacheData(launches: List<Launch>): DataResult<List<Launch>> {
        return when (val cacheResult = insertLaunchesToCacheUseCase(launches)) {
            is DataResult.Success -> DataResult.Success(launches)
            is DataResult.Error -> DataResult.Error(cacheResult.exception)
            else -> DataResult.Error(UNKNOWN_DATABASE_ERROR)
        }
    }
}