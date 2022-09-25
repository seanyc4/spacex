package com.seancoyle.launch_usecases.company

import com.seancoyle.launch_models.model.company.CompanyInfoModel
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.core.state.*
import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.core.network.ApiResponseHandler
import com.seancoyle.launch_datasource.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.core.network.safeApiCall
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.launch_viewstate.LaunchViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCompanyInfoFromNetworkAndInsertToCacheUseCase
constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: CompanyInfoCacheDataSource,
    private val networkDataSource: CompanyInfoNetworkDataSource,
    private val factory: CompanyInfoFactory
) {

    operator fun invoke(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            networkDataSource.getCompanyInfo()
        }

        val networkResponse = object : ApiResponseHandler<LaunchViewState, CompanyInfoModel?>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfoModel?): DataState<LaunchViewState> {
                return if (resultObj != null) {
                    val viewState =
                        LaunchViewState(
                            company = resultObj
                        )
                    DataState.data(
                        response = null,
                        data = viewState,
                        stateEvent = null
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = COMPANY_INFO_EMPTY,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchViewState> {
                return DataState.error(
                    response = Response(
                        message = COMPANY_INFO_ERROR,
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        if (networkResponse?.stateMessage?.response?.message?.equals(COMPANY_INFO_ERROR) == true) {
            emit(networkResponse)
        }

        // Insert to Cache
        if (networkResponse?.data != null) {

            val companyInfo = factory.createCompanyInfo(
                id = null,
                employees = networkResponse.data?.company?.employees!!,
                founded = networkResponse.data?.company?.founded!!,
                founder = networkResponse.data?.company?.founder!!,
                launchSites = networkResponse.data?.company?.launchSites!!,
                name = networkResponse.data?.company?.name!!,
                valuation = networkResponse.data?.company?.valuation!!,
            )

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insert(companyInfo)
            }

            val cacheResponse = object : CacheResponseHandler<LaunchViewState, Long>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Long): DataState<LaunchViewState> {
                    return if (resultObj > 0) {
                        DataState.data(
                            response = Response(
                                message = COMPANY_INFO_INSERT_SUCCESS,
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Success
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = COMPANY_INFO_INSERT_FAILED,
                                uiComponentType = UIComponentType.None,
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
    }

    companion object {
        const val COMPANY_INFO_EMPTY = "No data returned from network."
        const val COMPANY_INFO_ERROR = "Please check your internet connection and try again.\n\nReason: Network error"
        const val COMPANY_INFO_INSERT_SUCCESS = "Successfully inserted company info from network."
        const val COMPANY_INFO_INSERT_FAILED = "Failed to insert company info from network."
    }
}