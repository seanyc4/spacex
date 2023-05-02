package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateEvent
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.usecase.InsertLaunchListToCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertLaunchListToCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource
) : InsertLaunchListToCacheUseCase {

    override operator fun invoke(
        launchList: List<LaunchModel>,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {


        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.insertList(launchList)
        }

        val cacheResponse = object : CacheResponseHandler<LaunchViewState, LongArray>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: LongArray): DataState<LaunchViewState> {
                return if (resultObj.isNotEmpty()) {
                    val viewState =
                        LaunchViewState(
                            launchList = launchList
                        )
                    DataState.data(
                        response = Response(
                            message = INSERT_LAUNCH_LIST_SUCCESS,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = INSERT_LAUNCH_LIST_FAILED,
                            uiComponentType = UIComponentType.Toast,
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

    companion object {
        const val INSERT_LAUNCH_LIST_SUCCESS = "Successfully inserted new launch list."
        const val INSERT_LAUNCH_LIST_FAILED = "Failed to insert new launch list."
    }
}