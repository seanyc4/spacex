package com.seancoyle.spacex.business.interactors.company

import com.seancoyle.spacex.business.data.cache.CacheResponseHandler
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.data.util.safeCacheCall
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertCompanyInfoToCache(
    private val cacheDataSource: CompanyInfoCacheDataSource,
    private val factory: CompanyInfoFactory
) {

    fun execute(
        companyInfo: CompanyInfoModel,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val newCompanyInfo: CompanyInfoModel

        companyInfo.apply {
            newCompanyInfo = factory.createCompanyInfo(
                id = id,
                employees = employees,
                founded = founded,
                founder = founder,
                launchSites = launchSites,
                name = name,
                valuation = valuation
            )
        }

        val cacheResult = safeCacheCall(IO) {
            cacheDataSource.insert(newCompanyInfo)
        }

        val cacheResponse = object : CacheResponseHandler<LaunchViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<LaunchViewState> {
                return if (resultObj > 0) {
                    val viewState =
                        LaunchViewState(
                            company = newCompanyInfo
                        )
                    DataState.data(
                        response = Response(
                            message = INSERT_COMPANY_INFO_SUCCESS,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = INSERT_COMPANY_INFO_FAILED,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)
    }

    companion object {
        const val INSERT_COMPANY_INFO_SUCCESS = "Successfully inserted new company info."
        const val INSERT_COMPANY_INFO_FAILED = "Failed to insert new company info."
    }
}