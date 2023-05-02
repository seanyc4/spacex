package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateEvent
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.launch.api.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchViewState
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
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val cacheResult = safeCacheCall(ioDispatcher) {
            cacheDataSource.getCompanyInfo()
        }

        val response = object : CacheResponseHandler<LaunchViewState, CompanyInfoModel?>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfoModel?): DataState<LaunchViewState> {
                var message: String? =
                    GET_COMPANY_INFO_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj == null) {
                    message =
                        GET_COMPANY_INFO_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = LaunchViewState(
                        company = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        const val GET_COMPANY_INFO_SUCCESS = "Successfully retrieved company info"
        const val GET_COMPANY_INFO_NO_MATCHING_RESULTS =
            "There is no company info that matches that query."
    }
}







