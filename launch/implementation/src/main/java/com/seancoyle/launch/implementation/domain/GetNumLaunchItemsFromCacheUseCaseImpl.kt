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
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.usecase.GetNumLaunchItemsFromCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNumLaunchItemsFromCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchItemsFromCacheUseCase {

    override operator fun invoke(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.getTotalEntries()
        }
        val response = object : CacheResponseHandler<LaunchViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<LaunchViewState> {
                val viewState = LaunchViewState(
                    numLaunchItemsInCache = resultObj
                )
                return DataState.data(
                    response = Response(
                        message = GET_NUM_LAUNCH_ITEMS_SUCCESS,
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Success
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        const val GET_NUM_LAUNCH_ITEMS_SUCCESS =
            "Successfully retrieved the number of launch items from the cache."
    }
}