package com.seancoyle.core.data

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.data.CacheConstants.CACHE_TIMEOUT
import com.seancoyle.core.data.CacheErrors.CACHE_ERROR
import com.seancoyle.core.data.CacheErrors.CACHE_ERROR_TIMEOUT
import com.seancoyle.core.data.CacheErrors.CONSTRAINT_VIOLATION
import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
import com.seancoyle.core.data.NetworkConstants.NETWORK_TIMEOUT
import com.seancoyle.core.data.NetworkErrors.NETWORK_CONNECTION_FAILED
import com.seancoyle.core.data.NetworkErrors.NETWORK_ERROR_TIMEOUT
import com.seancoyle.core.data.NetworkErrors.NETWORK_ERROR_UNKNOWN
import com.seancoyle.core.data.NetworkErrors.NETWORK_FORBIDDEN
import com.seancoyle.core.data.NetworkErrors.NETWORK_INTERNAL_SERVER_ERROR
import com.seancoyle.core.data.NetworkErrors.NETWORK_NOT_FOUND
import com.seancoyle.core.data.NetworkErrors.NETWORK_UNAUTHORIZED
import com.seancoyle.core.data.NetworkErrors.UNKNOWN_NETWORK_ERROR
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): DataResult<T?> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                DataResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    DataResult.Error(NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    DataResult.Error(NETWORK_CONNECTION_FAILED)
                }
                is HttpException -> {
                    when (throwable.code()) {
                        401 -> DataResult.Error(NETWORK_UNAUTHORIZED)
                        403 -> DataResult.Error(NETWORK_FORBIDDEN)
                        404 -> DataResult.Error(NETWORK_NOT_FOUND)
                        500 -> DataResult.Error(NETWORK_INTERNAL_SERVER_ERROR)
                        else -> DataResult.Error("${NETWORK_ERROR_UNKNOWN}${throwable.code()}")                    }
                }

                else ->
                    DataResult.Error(throwable.message ?: UNKNOWN_NETWORK_ERROR)
            }
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
                is SQLiteConstraintException -> {
                    DataResult.Error(CONSTRAINT_VIOLATION)
                }
                is SQLiteException -> {
                    DataResult.Error("${CACHE_ERROR}: ${throwable.message}")
                }
                else -> {
                    DataResult.Error(throwable.message ?: UNKNOWN_DATABASE_ERROR)
                }
            }
        }
    }
}