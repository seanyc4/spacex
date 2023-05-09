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
import com.seancoyle.launch.api.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchState
import com.seancoyle.launch.api.usecase.GetCompanyInfoFromCacheUseCase
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
                var message: String? =
                   event.eventName() + EVENT_CACHE_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj == null) {
                    message =
                        event.eventName() + EVENT_CACHE_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
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







