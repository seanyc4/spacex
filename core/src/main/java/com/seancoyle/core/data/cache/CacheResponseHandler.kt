package com.seancoyle.core.data.cache

import com.seancoyle.core.data.cache.CacheErrors.CACHE_DATA_NULL
import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.MessageDisplayType
import com.seancoyle.core.domain.MessageType
import com.seancoyle.core.domain.Response
import com.seancoyle.core.domain.Result

abstract class CacheResponseHandler <UiState, Data>(
    private val response: CacheResult<Data?>
){
    suspend fun getResult(): DataState<UiState>? {

        return when(response){

            is CacheResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = response.errorMessage,
                        messageDisplayType = MessageDisplayType.None,
                        messageType = MessageType.Error
                    )
                )
            }

            is CacheResult.Success -> {
                if(response.value == null){
                    DataState.error(
                        response = Response(
                            message = CACHE_DATA_NULL,
                            messageDisplayType = MessageDisplayType.None,
                            messageType = MessageType.Error
                        )
                    )
                }
                else{
                    handleSuccess(data = response.value)
                }
            }

        }
    }

    abstract suspend fun handleSuccess(data: Data): DataState<UiState>?

}