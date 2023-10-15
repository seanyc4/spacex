package com.seancoyle.core.data.cache

import com.seancoyle.core.data.cache.CacheErrors.CACHE_DATA_NULL
import com.seancoyle.core.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.seancoyle.core.data.network.ApiResult

abstract class CacheResponseHandler<Data>(
    private val response: CacheResult<Data?>
) {
    suspend fun getResult(): ApiResult<Data>? {

        return when (response) {

            is CacheResult.Error -> {
                val message = response.errorMessage ?: CACHE_ERROR_UNKNOWN
                emitError(message)
            }

            is CacheResult.Success -> {
                if (response.value == null) {
                    emitError(CACHE_DATA_NULL)
                } else {
                    emitSuccess(response.value)
                }
            }
        }
    }

    abstract suspend fun emitError(errorMessage: String): ApiResult<Data>?

    abstract suspend fun emitSuccess(data: Data): ApiResult<Data>?
}