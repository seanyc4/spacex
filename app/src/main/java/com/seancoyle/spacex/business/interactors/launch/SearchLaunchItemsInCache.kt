package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheResponseHandler
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.data.util.safeCacheCall
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchLaunchItemsInCache(
    private val cacheDataSource: LaunchCacheDataSource
) {

    fun execute(
        query: String,
        order: String,
        isLaunchSuccess: Boolean?,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        var updatedPage = page
        if (page <= 0) {
            updatedPage = 1
        }

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.searchLaunchList(
                year = query,
                order = order,
                isLaunchSuccess = isLaunchSuccess,
                page = updatedPage
            )
        }

        val response = object : CacheResponseHandler<LaunchViewState, List<LaunchDomainEntity>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<LaunchDomainEntity>): DataState<LaunchViewState> {
                var message: String? =
                    SEARCH_LAUNCH_SUCCESS
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
                        launchList = resultObj as ArrayList<LaunchDomainEntity>?
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
        const val SEARCH_LAUNCH_FAILED = "Failed to retrieve the list of launch items."

    }
}







