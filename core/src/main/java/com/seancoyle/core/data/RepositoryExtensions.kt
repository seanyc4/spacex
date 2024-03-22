package com.seancoyle.core.data

import com.seancoyle.core.data.CacheConstants.CACHE_TIMEOUT
import com.seancoyle.core.data.CacheErrors.CACHE_ERROR_TIMEOUT
import com.seancoyle.core.data.CacheErrors.CACHE_ERROR_UNKNOWN
import com.seancoyle.core.data.NetworkConstants.NETWORK_TIMEOUT
import com.seancoyle.core.data.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.seancoyle.core.domain.UsecaseResponses.ERROR_UNKNOWN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): DataResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT){
                DataResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            DataResult.Error(throwable.message ?: NETWORK_ERROR_UNKNOWN)
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): DataResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT){
                DataResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    DataResult.Error(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    DataResult.Error(throwable.message ?: CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}