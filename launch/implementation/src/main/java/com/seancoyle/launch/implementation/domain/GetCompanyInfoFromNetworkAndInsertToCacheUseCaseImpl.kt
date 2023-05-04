package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.ApiResponseHandler
import com.seancoyle.core.network.safeApiCall
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.StateEvent
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.launch.api.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: CompanyInfoCacheDataSource,
    private val networkDataSource: CompanyInfoNetworkDataSource
) : GetCompanyInfoFromNetworkAndInsertToCacheUseCase {

    private var companyInfoModel: CompanyInfoModel? = null
    private var viewState: LaunchViewState = LaunchViewState()

    override operator fun invoke(
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
                    companyInfoModel = resultObj
                    viewState.company = resultObj
                    DataState.data(
                        response = null,
                        data = null,
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
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        networkResponse?.let {
            if (networkResponse.stateMessage?.response?.message == COMPANY_INFO_ERROR) {
                emit(networkResponse)
            }
        }

        // Insert to Cache
        if (companyInfoModel != null) {

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insert(companyInfoModel!!)
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
                            data = viewState,
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
        const val COMPANY_INFO_ERROR =
            "Please check your internet connection and try again.\n\nReason: Network error"
        const val COMPANY_INFO_INSERT_SUCCESS = "Successfully inserted company info from network."
        const val COMPANY_INFO_INSERT_FAILED = "Failed to insert company info from network."
    }
}