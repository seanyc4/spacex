package com.seancoyle.core.cache

import com.seancoyle.core.cache.CacheErrors.CACHE_DATA_NULL
import com.seancoyle.core.state.*

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
                        uiComponentType = UIComponentType.None,
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
                            uiComponentType = UIComponentType.None,
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