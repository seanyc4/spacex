package com.seancoyle.core.data.network

import com.seancoyle.core.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response

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