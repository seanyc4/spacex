package com.seancoyle.core.data.cache

import com.seancoyle.core.data.cache.CacheErrors.CACHE_DATA_NULL
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response

abstract class CacheResponseHandler <UiState, Data>(
    private val response: CacheResult<Data?>,
    private val event: Event?
){
    suspend fun getResult(): DataState<UiState>? {

        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = "${event?.errorInfo()}\n\nReason: ${response.errorMessage}",
                        messageDisplayType = MessageDisplayType.None,
                        messageType = MessageType.Error
                    ),
                    event = event
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = "${event?.errorInfo()}\n\nReason: ${CACHE_DATA_NULL}.",
                            messageDisplayType = MessageDisplayType.None,
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

}