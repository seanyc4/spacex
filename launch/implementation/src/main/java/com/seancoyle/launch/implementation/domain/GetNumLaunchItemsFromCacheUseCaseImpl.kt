package com.seancoyle.launch.implementation.domain


import com.seancoyle.core.data.cache.CacheResponseHandler
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.api.presentation.LaunchUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNumLaunchItemsFromCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchItemsFromCacheUseCase {

    override operator fun invoke(): Flow<DataState<LaunchUiState.LaunchState>?> = flow {

        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.getTotalEntries()
        }
        val response = object : CacheResponseHandler<LaunchUiState.LaunchState, Int>(
            response = cacheResult
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<LaunchUiState.LaunchState> {
                val viewState = LaunchUiState.LaunchState(
                    numLaunchesInCache = resultObj
                )
                return DataState.success(
                    response = Response(
                        message = EVENT_CACHE_SUCCESS,
                        messageDisplayType = MessageDisplayType.None,
                        messageType = MessageType.Success
                    ),
                    data = viewState
                )
            }
        }.getResult()
        emit(response)
    }
}