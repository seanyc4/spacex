package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import com.seancoyle.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetLaunchesFromNetworkAndInsertToCacheUseCase {

    override operator fun invoke(): Flow<DataResult<List<Launch>>> = flow {
        val result = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        }

        when (result) {
            is DataResult.Success -> {
                result.data?.let { launches ->
                    safeCacheCall(ioDispatcher) {
                        cacheDataSource.insertList(launches)
                    }
                    emit(DataResult.Success(launches))
                }
            }

            is DataResult.Error -> {
                emit(result)
            }

            else -> {}
        }
    }
}