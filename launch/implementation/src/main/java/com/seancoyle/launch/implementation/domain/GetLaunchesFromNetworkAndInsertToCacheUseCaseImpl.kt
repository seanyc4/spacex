package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.cache.CacheResponseHandler
import com.seancoyle.core.data.network.ApiResponseHandler
import com.seancoyle.core.data.network.safeApiCall
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_EMPTY
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.launch.contract.data.LaunchCacheDataSource
import com.seancoyle.launch.contract.data.LaunchNetworkDataSource
import com.seancoyle.launch.contract.domain.model.LaunchOptions
import com.seancoyle.launch.contract.domain.model.LaunchState
import com.seancoyle.launch.contract.domain.model.ViewModel
import com.seancoyle.launch.contract.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
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

    private var launchList: List<ViewModel> = emptyList()

    override operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        }

        val networkResponse = object : ApiResponseHandler<LaunchState, List<ViewModel>?>(
            response = networkResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: List<ViewModel>?): DataState<LaunchState> {
                return if (resultObj != null) {
                    launchList = resultObj
                    DataState.data(
                        response = null,
                        data = null,
                        event = null
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = event.eventName() + EVENT_NETWORK_EMPTY,
                            messageDisplayType = MessageDisplayType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        event = event
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchState> {
                return DataState.error(
                    response = Response(
                        message = event.eventName() + EVENT_NETWORK_ERROR,
                        messageDisplayType = MessageDisplayType.Toast,
                        messageType = MessageType.Error
                    ),
                    event = event
                )
            }
        }.getResult()

        networkResponse?.let {
            if (networkResponse.stateMessage?.response?.message == event.eventName() + EVENT_NETWORK_ERROR) {
                emit(networkResponse)
            }
        }

        // Insert to Cache
        if (launchList.isNotEmpty()) {

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insertList(launchList)
            }

            val cacheResponse = object : CacheResponseHandler<LaunchState, LongArray>(
                response = cacheResult,
                event = event
            ) {
                override suspend fun handleSuccess(resultObj: LongArray): DataState<LaunchState> {
                    return if (resultObj.isNotEmpty()) {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_SUCCESS,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Success
                            ),
                            event = event
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_FAILED,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Error
                            ),
                            event = event
                        )
                    }
                }
            }.getResult()
            emit(cacheResponse)
        }
    }
}