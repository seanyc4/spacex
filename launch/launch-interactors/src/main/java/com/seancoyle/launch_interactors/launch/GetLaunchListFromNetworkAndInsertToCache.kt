package com.seancoyle.launch_interactors.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.launch_domain.model.launch.LaunchFactory
import com.seancoyle.core.state.*
import com.seancoyle.launch_datasource.cache.CacheResponseHandler
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource.network.ApiResponseHandler
import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_datasource.util.safeApiCall
import com.seancoyle.launch_datasource.util.safeCacheCall
import com.seancoyle.launch_domain.model.launch.LaunchOptions
import com.seancoyle.launch_viewstate.LaunchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLaunchListFromNetworkAndInsertToCache
constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val factory: LaunchFactory
) {

    fun execute(
        launchOptions: LaunchOptions,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val networkResult = safeApiCall(Dispatchers.IO) {
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
        if (networkResponse?.data != null) {

            val launchList = factory.createLaunchList(
                networkResponse.data?.launchList!!
            )

            val cacheResult = safeCacheCall(Dispatchers.IO) {
                cacheDataSource.insertLaunchList(launchList)
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
        const val LAUNCH_ERROR = "Error updating launch items from network.\n\nReason: Network error"
        const val LAUNCH_INSERT_SUCCESS = "Successfully inserted launch items from network."
        const val LAUNCH_INSERT_FAILED = "Failed to insert launch items from network."
    }
}