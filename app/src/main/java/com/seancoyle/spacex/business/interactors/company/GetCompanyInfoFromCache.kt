package com.seancoyle.spacex.business.interactors.company

import com.seancoyle.spacex.business.data.cache.CacheResponseHandler
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.data.util.safeCacheCall
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
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

        val response = object : CacheResponseHandler<LaunchViewState, CompanyInfoDomainEntity?>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfoDomainEntity?): DataState<LaunchViewState> {
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
        const val GET_COMPANY_INFO_FAILED = "Failed to retrieve company info."
        const val GET_COMPANY_INFO_NO_DATA =
            "Error getting company info from cache.\n\nReason: Cache data is null."
    }
}







