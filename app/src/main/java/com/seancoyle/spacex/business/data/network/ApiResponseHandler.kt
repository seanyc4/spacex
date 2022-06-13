package com.seancoyle.spacex.business.data.network

import com.seancoyle.spacex.business.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.domain.state.*


abstract class ApiResponseHandler <ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
){

    suspend fun getResult(): DataState<ViewState>? {

        return when(response){

            is ApiResult.GenericError -> {
                handleFailure()
            }

            is ApiResult.NetworkError -> {
                handleFailure()
            }

            is ApiResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${stateEvent?.errorInfo()}\n\nReason: ${NETWORK_DATA_NULL}.",
                            uiComponentType = UIComponentType.Dialog,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?

    abstract suspend fun handleFailure(): DataState<ViewState>?

}