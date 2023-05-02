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
import com.seancoyle.launch.api.usecase.FilterLaunchItemsInCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilterLaunchItemsInCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource
) : FilterLaunchItemsInCacheUseCase {

    override operator fun invoke(
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

        val cacheResult = safeCacheCall(ioDispatcher) {
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







