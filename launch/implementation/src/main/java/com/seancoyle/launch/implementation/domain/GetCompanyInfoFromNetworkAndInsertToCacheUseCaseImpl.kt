package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.cache.CacheResponseHandler
import com.seancoyle.core.data.network.ApiResponseHandler
import com.seancoyle.core.data.network.safeApiCall
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_EMPTY
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfoModel
import com.seancoyle.launch.api.domain.model.LaunchState
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
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
    private var viewState: LaunchState = LaunchState()

    override operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            networkDataSource.getCompanyInfo()
        }

        val networkResponse = object : ApiResponseHandler<LaunchState, CompanyInfoModel?>(
            response = networkResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfoModel?): DataState<LaunchState> {
                return if (resultObj != null) {
                    companyInfoModel = resultObj
                    viewState.company = resultObj
                    DataState.data(
                        response = null,
                        data = null,
                        event = null
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = event.eventName() + EVENT_NETWORK_EMPTY,
                            messageDisplayType = MessageDisplayType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        event = event
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchState> {
                return DataState.error(
                    response = Response(
                        message = event.eventName() + EVENT_NETWORK_ERROR,
                        messageDisplayType = MessageDisplayType.Toast,
                        messageType = MessageType.Error
                    ),
                    event = event
                )
            }
        }.getResult()

        networkResponse?.let {
            if (networkResponse.stateMessage?.response?.message == event.eventName() + EVENT_NETWORK_ERROR) {
                emit(networkResponse)
            }
        }

        // Insert to Cache
        if (companyInfoModel != null) {

            val cacheResult = safeCacheCall(ioDispatcher) {
                cacheDataSource.insert(companyInfoModel!!)
            }

            val cacheResponse = object : CacheResponseHandler<LaunchState, Long>(
                response = cacheResult,
                event = event
            ) {
                override suspend fun handleSuccess(resultObj: Long): DataState<LaunchState> {
                    return if (resultObj > 0) {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_SUCCESS,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Success
                            ),
                            data = viewState,
                            event = event
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_FAILED,
                                messageDisplayType = MessageDisplayType.None,
                                messageType = MessageType.Error
                            ),
                            data = null,
                            event = event
                        )
                    }
                }
            }.getResult()
            emit(cacheResponse)
        }
    }
}