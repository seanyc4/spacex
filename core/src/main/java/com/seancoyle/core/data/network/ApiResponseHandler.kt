package com.seancoyle.core.data.network

import com.seancoyle.core.data.network.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.data.network.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.seancoyle.core.domain.Result

abstract class ApiResponseHandler<Data>(
    private val response: ApiResult<Data?>
) {

    suspend fun getResult(): Result<Data>? {

        return when (response) {

            is ApiResult.Error -> {
                val message = response.errorMessage ?: NETWORK_ERROR_UNKNOWN
                emitError(message)
            }

            is ApiResult.Success -> {
                if (response.value == null) {
                    emitError(NETWORK_DATA_NULL)
                } else {
                    emitSuccess(data = response.value)
                }
            }

            ApiResult.NetworkError -> TODO()
        }
    }

    abstract suspend fun emitError(errorMessage: String): Result<Data>?

    abstract suspend fun emitSuccess(data: Data): Result<Data>?
}