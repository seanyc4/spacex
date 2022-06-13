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

class GetAllLaunchItemsFromCache(
    private val cacheDataSource: LaunchCacheDataSource
){

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            cacheDataSource.getAll()
        }

        val response = object: CacheResponseHandler<LaunchViewState, List<LaunchModel>?>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<LaunchModel>?): DataState<LaunchViewState> {
                var message: String? =
                    GET_ALL_LAUNCH_ITEMS_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if(resultObj == null){
                    message =
                        GET_ALL_LAUNCH_ITEMS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = LaunchViewState(
                        launchList = resultObj as ArrayList<LaunchModel>?
                    ),
                    stateEvent = stateEvent
                )
            }


        }.getResult()

        emit(response)
    }

    companion object{
        const val GET_ALL_LAUNCH_ITEMS_SUCCESS = "Successfully retrieved all launch items"
        const val GET_ALL_LAUNCH_ITEMS_NO_MATCHING_RESULTS = "There are no launch items that match that query."
    }
}







