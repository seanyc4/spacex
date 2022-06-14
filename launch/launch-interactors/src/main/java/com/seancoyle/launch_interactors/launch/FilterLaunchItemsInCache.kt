package com.seancoyle.launch_interactors.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel
import com.seancoyle.core.state.*
import com.seancoyle.launch_datasource.cache.CacheResponseHandler
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource.util.safeCacheCall
import com.seancoyle.launch_viewstate.LaunchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FilterLaunchItemsInCache(
    private val cacheDataSource: LaunchCacheDataSource
) {

    fun execute(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        var updatedPage = page
        if (page <= 0) {
            updatedPage = 1
        }

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.filterLaunchList(
                year = year,
                order = order,
                launchFilter = launchFilter,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<LaunchViewState, List<LaunchModel>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<LaunchModel>): DataState<LaunchViewState> {
                var message: String? = SEARCH_LAUNCH_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj.isEmpty()) {
                    message =
                        SEARCH_LAUNCH_NO_MATCHING_RESULTS
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

    companion object {
        const val SEARCH_LAUNCH_SUCCESS = "Successfully retrieved list of launch items."
        const val SEARCH_LAUNCH_NO_MATCHING_RESULTS =
            "There are no launch items that match that query."
    }
}







