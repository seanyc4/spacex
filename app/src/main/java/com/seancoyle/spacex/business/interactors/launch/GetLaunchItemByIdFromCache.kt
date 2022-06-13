package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheResponseHandler
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.core.domain.state.*
import com.seancoyle.spacex.business.data.util.safeCacheCall
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLaunchItemByIdFromCache(
    private val cacheDataSource: LaunchCacheDataSource
) {

    fun execute(
        id: Int,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.getById(
                id = id
            )
        }

        val response = object : CacheResponseHandler<LaunchViewState, LaunchModel?>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: LaunchModel?): DataState<LaunchViewState> {
                var message: String? =
                    GET_LAUNCH_ITEM_BY_ID_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj == null) {
                    message =
                        GET_LAUNCH_ITEM_BY_ID_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = LaunchViewState(
                        launch = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        const val GET_LAUNCH_ITEM_BY_ID_SUCCESS = "Successfully retrieved launch item by id"
        const val GET_LAUNCH_ITEM_BY_ID_NO_MATCHING_RESULTS =
            "There are no launch items that match that query."
    }
}







