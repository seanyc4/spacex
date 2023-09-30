package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.cache.CacheResponseHandler
import com.seancoyle.core.data.network.ApiResponseHandler
import com.seancoyle.core.data.network.safeApiCall
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_EMPTY
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.api.presentation.LaunchUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions
) : GetLaunchesFromNetworkAndInsertToCacheUseCase {

    private var launchList: List<Launch> = emptyList()

    override operator fun invoke(): Flow<DataState<LaunchUiState.LaunchState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        }

        val networkResponse = object : ApiResponseHandler<LaunchUiState.LaunchState, List<Launch>?>(
            response = networkResult
        ) {
            override suspend fun handleSuccess(resultObj: List<Launch>?): DataState<LaunchUiState.LaunchState> {
                return if (resultObj != null) {
                    launchList = resultObj
                    DataState.success(
                        response = null,
                        data = null
                    )
                } else {
                    DataState.success(
                        response = Response(
                            message = EVENT_NETWORK_EMPTY,
                            messageDisplayType = MessageDisplayType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchUiState.LaunchState> {
                return DataState.error(
                    response = Response(
                        message = EVENT_NETWORK_ERROR,
                        messageDisplayType = MessageDisplayType.Toast,
                        messageType = MessageType.Error
                    )
                )
            }
        }.getResult()

        networkResponse?.let {
            if (networkResponse.stateMessage?.response?.message == EVENT_NETWORK_ERROR) {
                emit(networkResponse)
            }
        }

        // Insert to Cache
        if (launchList.isNotEmpty()) {

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insertList(launchList)
            }

            val cacheResponse = object : CacheResponseHandler<LaunchUiState.LaunchState, LongArray>(
                response = cacheResult
            ) {
                override suspend fun handleSuccess(data: LongArray): DataState<LaunchUiState.LaunchState> {
                    return if (data.isNotEmpty()) {
                        DataState.success(
                            response = Response(
                                message = EVENT_CACHE_INSERT_SUCCESS,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Success
                            )
                        )
                    } else {
                        DataState.success(
                            response = Response(
                                message = EVENT_CACHE_INSERT_FAILED,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Error
                            )
                        )
                    }
                }
            }.getResult()
            emit(cacheResponse)
        }
    }
}