package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.data.NetworkErrors.UNKNOWN_NETWORK_ERROR
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val insertLaunchesToCacheUseCase: InsertLaunchesToCacheUseCase,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(): Flow<DataResult<List<Launch>>> = flow {
        val result = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions)
        }

        when (result) {
            is DataResult.Success -> {
                result.data?.let { launches ->
                    when (val insertResult = insertLaunchesToCacheUseCase(launches)) {
                        is DataResult.Success -> {
                            emit(DataResult.Success(launches))
                        }

                        is DataResult.Error -> {
                            emit(DataResult.Error(insertResult.exception))
                        }

                        else -> {
                            emit(DataResult.Error(UNKNOWN_DATABASE_ERROR))
                        }
                    }
                } ?: emit(DataResult.Error(NETWORK_DATA_NULL))
            }

            is DataResult.Error -> {
                emit(DataResult.Error(result.exception))
            }

            else -> {
                emit(DataResult.Error(UNKNOWN_NETWORK_ERROR))
            }
        }
    }
}