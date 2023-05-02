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
import com.seancoyle.launch.api.model.CompanyInfoFactory
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchViewState
import com.seancoyle.launch.api.usecase.InsertCompanyInfoToCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertCompanyInfoToCacheUseCaseImpl @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val cacheDataSource: CompanyInfoCacheDataSource,
    private val factory: CompanyInfoFactory
) : InsertCompanyInfoToCacheUseCase {

    override operator fun invoke(
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

        val cacheResult = safeCacheCall(ioDispatcher) {
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