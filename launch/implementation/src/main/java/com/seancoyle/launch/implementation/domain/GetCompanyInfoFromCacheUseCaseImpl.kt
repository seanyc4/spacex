package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.cache.CacheResponseHandler
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_NO_MATCHING_RESULTS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_SUCCESS
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.LaunchState
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCompanyInfoFromCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: CompanyInfoCacheDataSource
) : GetCompanyInfoFromCacheUseCase {

    override operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?> = flow {

        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.getCompanyInfo()
        }

        val response = object : CacheResponseHandler<LaunchState, CompanyInfo?>(
            response = cacheResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfo?): DataState<LaunchState> {
                val (resultMessage, uiComponentType) = if (resultObj == null) {
                    Pair(EVENT_CACHE_NO_MATCHING_RESULTS, MessageDisplayType.Toast)
                } else {
                    Pair(EVENT_CACHE_SUCCESS, MessageDisplayType.None)
                }
                val message = event.eventName() + resultMessage
                return DataState.data(
                    response = Response(
                        message = message,
                        messageDisplayType = uiComponentType,
                        messageType = MessageType.Success
                    ),
                    data = LaunchState(
                        company = resultObj
                    ),
                    event = event
                )
            }
        }.getResult()
        emit(response)
    }
}







