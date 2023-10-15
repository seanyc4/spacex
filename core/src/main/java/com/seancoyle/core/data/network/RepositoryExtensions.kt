package com.seancoyle.core.data.network

import com.seancoyle.core.data.cache.CacheConstants.CACHE_TIMEOUT
import com.seancoyle.core.data.cache.CacheErrors.CACHE_ERROR_TIMEOUT
import com.seancoyle.core.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import com.seancoyle.core.data.cache.CacheResult
import com.seancoyle.core.data.network.NetworkConstants.NETWORK_TIMEOUT
import com.seancoyle.core.domain.UsecaseResponses.ERROR_UNKNOWN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT){
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
           ApiResult.Error(throwable)
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
): CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT){
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {

                is TimeoutCancellationException -> {
                    CacheResult.Error(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.Error(CACHE_ERROR_UNKNOWN)
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