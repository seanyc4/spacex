package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheResponseHandler
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.data.util.safeCacheCall
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumLaunchItemsFromCache(
    private val cacheDataSource: LaunchCacheDataSource
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val cacheResult = safeCacheCall(IO) {
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
        const val GET_NUM_LAUNCH_ITEMS_SUCCESS = "Successfully retrieved the number of launch items from the cache."
    }
}