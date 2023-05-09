package com.seancoyle.core.network

import com.seancoyle.core.network.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.state.*

abstract class ApiResponseHandler <UiState, Data>(
    private val response: ApiResult<Data?>,
    private val event: Event?
){

    suspend fun getResult(): DataState<UiState>? {

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
                            message = "${event?.errorInfo()}\n\nReason: ${NETWORK_DATA_NULL}.",
                            messageDisplayType = MessageDisplayType.Dialog,
                            messageType = MessageType.Error
                        ),
                        event = event
                    )
                }
                else{
                    handleSuccess(resultObj = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<UiState>?

    abstract suspend fun handleFailure(): DataState<UiState>?

}