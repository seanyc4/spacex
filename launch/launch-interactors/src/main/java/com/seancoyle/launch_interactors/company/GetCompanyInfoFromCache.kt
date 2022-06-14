package com.seancoyle.launch_interactors.company

import com.seancoyle.core.state.*
import com.seancoyle.launch_domain.model.company.CompanyInfoModel
import com.seancoyle.launch_datasource.cache.CacheResponseHandler
import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.util.safeCacheCall
import com.seancoyle.launch_viewstate.LaunchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCompanyInfoFromCache(
    private val cacheDataSource: CompanyInfoCacheDataSource
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
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







