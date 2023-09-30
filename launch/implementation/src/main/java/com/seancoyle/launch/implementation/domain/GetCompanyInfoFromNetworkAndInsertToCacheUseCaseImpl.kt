package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.cache.CacheResponseHandler
import com.seancoyle.core.data.network.ApiResponseHandler
import com.seancoyle.core.data.network.safeApiCall
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_EMPTY
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.api.presentation.LaunchUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: CompanyInfoCacheDataSource,
    private val networkDataSource: CompanyInfoNetworkDataSource
) : GetCompanyInfoFromNetworkAndInsertToCacheUseCase {

    private var companyInfo: CompanyInfo? = null
    private var viewState = LaunchUiState.LaunchState()

    override operator fun invoke(
    ): Flow<DataState<LaunchUiState.LaunchState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            networkDataSource.getCompanyInfo()
        }

        val networkResponse = object : ApiResponseHandler<LaunchUiState.LaunchState, CompanyInfo?>(
            response = networkResult
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfo?): DataState<LaunchUiState.LaunchState> {
                return if (resultObj != null) {
                    companyInfo = resultObj
                    viewState.company = resultObj
                    DataState.success(
                        response = null,
                        data = null
                    )
                } else {
                    DataState.success(
                        response = Response(
                            message = EVENT_NETWORK_EMPTY,
                            messageDisplayType = MessageDisplayType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchUiState.LaunchState> {
                return DataState.error(
                    response = Response(
                        message = EVENT_NETWORK_ERROR,
                        messageDisplayType = MessageDisplayType.Toast,
                        messageType = MessageType.Error
                    ),
                )
            }
        }.getResult()

        networkResponse?.let {
            if (networkResponse.stateMessage?.response?.message == EVENT_NETWORK_ERROR) {
                emit(networkResponse)
            }
        }

        // Insert to Cache
        if (companyInfo != null) {

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insert(companyInfo!!)
            }

            val cacheResponse = object : CacheResponseHandler<LaunchUiState.LaunchState, Long>(
                response = cacheResult
            ) {
                override suspend fun handleSuccess(data: Long): DataState<LaunchUiState.LaunchState> {
                    return if (data > 0) {
                        DataState.success(
                            response = Response(
                                message = EVENT_CACHE_INSERT_SUCCESS,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Success
                            ),
                            data = viewState
                        )
                    } else {
                        DataState.success(
                            response = Response(
                                message = EVENT_CACHE_INSERT_FAILED,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Error
                            ),
                            data = null
                        )
                    }
                }
            }.getResult()
            emit(cacheResponse)
        }
    }
}