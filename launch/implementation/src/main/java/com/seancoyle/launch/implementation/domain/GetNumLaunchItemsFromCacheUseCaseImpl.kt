package com.seancoyle.launch.implementation.domain


import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.state.MessageDisplayType
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_SUCCESS
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.LaunchState
import com.seancoyle.launch.api.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNumLaunchItemsFromCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchItemsFromCacheUseCase {

    override operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?> = flow {

        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.getTotalEntries()
        }
        val response = object : CacheResponseHandler<LaunchState, Int>(
            response = cacheResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<LaunchState> {
                val viewState = LaunchState(
                    numLaunchesInCache = resultObj
                )
                return DataState.data(
                    response = Response(
                        message = event.eventName() + EVENT_CACHE_SUCCESS,
                        messageDisplayType = MessageDisplayType.None,
                        messageType = MessageType.Success
                    ),
                    data = viewState,
                    event = event
                )
            }
        }.getResult()
        emit(response)
    }
}