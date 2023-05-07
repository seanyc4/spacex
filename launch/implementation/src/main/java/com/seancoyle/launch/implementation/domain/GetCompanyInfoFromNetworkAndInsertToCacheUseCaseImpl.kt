package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.cache.CacheResponseHandler
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.network.ApiResponseHandler
import com.seancoyle.core.network.safeApiCall
import com.seancoyle.core.network.safeCacheCall
import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.core.state.MessageType
import com.seancoyle.core.state.Response
import com.seancoyle.core.state.UIComponentType
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_INSERT_FAILED
import com.seancoyle.core.util.GenericErrors.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.util.GenericErrors.EVENT_NETWORK_EMPTY
import com.seancoyle.core.util.GenericErrors.EVENT_NETWORK_ERROR
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
        event: Event
    ): Flow<DataState<LaunchViewState>?> = flow {

        val networkResult = safeApiCall(ioDispatcher) {
            networkDataSource.getCompanyInfo()
        }

        val networkResponse = object : ApiResponseHandler<LaunchViewState, CompanyInfoModel?>(
            response = networkResult,
            event = event
        ) {
            override suspend fun handleSuccess(resultObj: CompanyInfoModel?): DataState<LaunchViewState> {
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
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        event = event
                    )
                }
            }

            override suspend fun handleFailure(): DataState<LaunchViewState> {
                return DataState.error(
                    response = Response(
                        message = event.eventName() + EVENT_NETWORK_ERROR,
                        uiComponentType = UIComponentType.Toast,
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

            val cacheResponse = object : CacheResponseHandler<LaunchViewState, Long>(
                response = cacheResult,
                event = event
            ) {
                override suspend fun handleSuccess(resultObj: Long): DataState<LaunchViewState> {
                    return if (resultObj > 0) {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_SUCCESS,
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Success
                            ),
                            data = viewState,
                            event = event
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = event.eventName() + EVENT_CACHE_INSERT_FAILED,
                                uiComponentType = UIComponentType.None,
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