package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.ApiResponseHandler
import com.seancoyle.core.network.safeApiCall
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.util.GenericErrors.EVENT_NETWORK_EMPTY
import com.seancoyle.core.util.GenericErrors.EVENT_NETWORK_ERROR
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.LaunchNetworkDataSource
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.api.model.LaunchState
import com.seancoyle.launch.api.usecase.GetLaunchListFromNetworkAndInsertToCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchOptions: LaunchOptions
) : GetLaunchListFromNetworkAndInsertToCacheUseCase {

    private var launchList: List<LaunchModel> = emptyList()

    override operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        }

        val networkResponse = object : ApiResponseHandler<LaunchState, List<LaunchModel>?>(
            response = networkResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: List<LaunchModel>?): DataState<LaunchState> {
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
                            uiComponentType = UIComponentType.Toast,
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
                        uiComponentType = UIComponentType.Toast,
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
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Success
                            ),
                            event = event
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_FAILED,
                                uiComponentType = UIComponentType.None,
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