package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.core.data.network.safeApiCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
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

    override operator fun invoke(): Flow<ApiResult<List<Launch>>> = flow {
        val result = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        }

        when (result) {
            is ApiResult.Success -> {
                result.data?.let { launches ->
                    cacheDataSource.insertList(launches)
                    emit(ApiResult.Success(launches))
                }
            }
            is ApiResult.Error -> {
                emit(result)
            }

            else -> {}
        }
    }
}