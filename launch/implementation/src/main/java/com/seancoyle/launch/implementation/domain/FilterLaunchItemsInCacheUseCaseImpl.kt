package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_NO_MATCHING_RESULTS
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_SUCCESS
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchState
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
        event: Event
    ): Flow<DataState<LaunchState>?> = flow {

        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.filterLaunchList(
                year = year,
                order = order,
                launchFilter = launchFilter,
                page = page
            )
        }

        val response = object : CacheResponseHandler<LaunchState, List<LaunchModel>>(
            response = cacheResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: List<LaunchModel>): DataState<LaunchState> {
                val (resultMessage, uiComponentType) = if (resultObj.isEmpty()) {
                    Pair(EVENT_CACHE_NO_MATCHING_RESULTS, UIComponentType.Toast)
                } else {
                    Pair(EVENT_CACHE_SUCCESS, UIComponentType.None)
                }
                val message = event.eventName() + resultMessage

                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType,
                        messageType = MessageType.Success
                    ),
                    data = LaunchState(
                        launches = resultObj
                    ),
                    event = event
                )
            }
        }.getResult()
        emit(response)
    }
}







