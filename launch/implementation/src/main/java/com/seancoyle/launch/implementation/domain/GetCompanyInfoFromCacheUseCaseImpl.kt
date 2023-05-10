package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.state.MessageDisplayType
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_NO_MATCHING_RESULTS
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_SUCCESS
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfoModel
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

        val response = object : CacheResponseHandler<LaunchState, CompanyInfoModel?>(
            response = cacheResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfoModel?): DataState<LaunchState> {
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







