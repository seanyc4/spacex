package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.ApiResponseHandler
import com.seancoyle.core.network.safeApiCall
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateEvent
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.LaunchNetworkDataSource
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.api.model.LaunchViewState
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

    override operator fun invoke(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        }

        val networkResponse = object : ApiResponseHandler<LaunchViewState, List<LaunchModel>?>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<LaunchModel>?): DataState<LaunchViewState> {
                return if (resultObj != null) {
                    val viewState =
                        LaunchViewState(
                            launchList = resultObj
                        )
                    DataState.data(
                        response = null,
                        data = viewState,
                        stateEvent = null
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = LAUNCH_NETWORK_EMPTY,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchViewState> {
                return DataState.error(
                    response = Response(
                        message = LAUNCH_ERROR,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        if (networkResponse?.stateMessage?.response?.message == LAUNCH_ERROR) {
            emit(networkResponse)
        }

        // Insert to Cache
        if (networkResponse?.data?.launchList != null) {

            val launchList = networkResponse.data?.launchList!!

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insertList(launchList)
            }

            val cacheResponse = object : CacheResponseHandler<LaunchViewState, LongArray>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: LongArray): DataState<LaunchViewState> {
                    return if (resultObj.isNotEmpty()) {
                        DataState.data(
                            response = Response(
                                message = LAUNCH_INSERT_SUCCESS,
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Success
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = LAUNCH_INSERT_FAILED,
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Error
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    }
                }
            }.getResult()
            emit(cacheResponse)
        }
    }

    companion object {
        const val LAUNCH_NETWORK_EMPTY = "No data returned from network."
        const val LAUNCH_ERROR =
            "Error updating launch items from network.\n\nReason: Network error"
        const val LAUNCH_INSERT_SUCCESS = "Successfully inserted launch items from network."
        const val LAUNCH_INSERT_FAILED = "Failed to insert launch items from network."
    }
}