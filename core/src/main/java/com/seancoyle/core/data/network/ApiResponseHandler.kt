package com.seancoyle.core.data.network

import com.seancoyle.core.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.data.network.NetworkErrors.NETWORK_ERROR_UNKNOWN

abstract class ApiResponseHandler<Data>(
    private val response: ApiResult_bkup<Data?>
) {

    suspend fun getResult(): ApiResult<Data>? {

        return when (response) {

            is ApiResult_bkup.Error -> {
                val message = response.errorMessage ?: NETWORK_ERROR_UNKNOWN
                emitError(message)
            }

            is ApiResult_bkup.Success -> {
                if (response.value == null) {
                    emitError(NETWORK_DATA_NULL)
                } else {
                    emitSuccess(data = response.value)
                }
            }

            ApiResult_bkup.NetworkError -> TODO()
        }
    }

    abstract suspend fun emitError(errorMessage: String): ApiResult<Data>?

    abstract suspend fun emitSuccess(data: Data): ApiResult<Data>?
}