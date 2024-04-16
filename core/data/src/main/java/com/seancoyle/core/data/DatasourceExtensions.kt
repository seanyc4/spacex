package com.seancoyle.core.data

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.data.CacheConstants.CACHE_TIMEOUT
import com.seancoyle.core.data.NetworkConstants.NETWORK_TIMEOUT
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

/**
 *  Adapted from: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    crashlytics: Crashlytics? = null,
    apiCall: suspend () -> T
): Result<T, DataError> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                Result.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            crashlytics?.logException(throwable)
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    Result.Error(DataError.NETWORK_TIMEOUT)
                }

                is IOException -> {
                    Result.Error(DataError.NETWORK_CONNECTION_FAILED)
                }

                is HttpException -> {
                    when (throwable.code()) {
                        401 -> Result.Error(DataError.NETWORK_UNAUTHORIZED)
                        403 -> Result.Error(DataError.NETWORK_FORBIDDEN)
                        404 -> Result.Error(DataError.NETWORK_NOT_FOUND)
                        408 -> Result.Error(DataError.NETWORK_TIMEOUT)
                        413 -> Result.Error(DataError.NETWORK_PAYLOAD_TOO_LARGE)
                        500 -> Result.Error(DataError.NETWORK_INTERNAL_SERVER_ERROR)
                        else -> Result.Error(DataError.NETWORK_UNKNOWN_ERROR)
                    }
                }

                is CancellationException -> {
                    // Don't catch cancellation exceptions
                    throw throwable
                }

                else ->
                    Result.Error(DataError.NETWORK_UNKNOWN_ERROR)
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    crashlytics: Crashlytics? = null,
    cacheCall: suspend () -> T
): Result<T, DataError> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                Result.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            crashlytics?.logException(throwable)
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    Result.Error(DataError.CACHE_ERROR_TIMEOUT)
                }

                is SQLiteConstraintException -> {
                    Result.Error(DataError.CACHE_CONSTRAINT_VIOLATION)
                }

                is SQLiteException -> {
                    Result.Error(DataError.CACHE_ERROR)
                }

                is CancellationException -> {
                    // Don't catch cancellation exceptions
                    throw throwable
                }

                else -> {
                    Result.Error(DataError.CACHE_UNKNOWN_DATABASE_ERROR)
                }
            }
        }
    }
}