package com.seancoyle.core.data

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.data.CacheConstants.CACHE_TIMEOUT
import com.seancoyle.core.data.NetworkConstants.NETWORK_TIMEOUT
import com.seancoyle.core.domain.Crashlytics
import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
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
): DataResult<T, DataError> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                DataResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            crashlytics?.logException(throwable)
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    DataResult.Error(DataError.NETWORK_TIMEOUT)
                }

                is IOException -> {
                    DataResult.Error(DataError.NETWORK_CONNECTION_FAILED)
                }

                is HttpException -> {
                    when (throwable.code()) {
                        401 -> DataResult.Error(DataError.NETWORK_UNAUTHORIZED)
                        403 -> DataResult.Error(DataError.NETWORK_FORBIDDEN)
                        408 -> DataResult.Error(DataError.NETWORK_TIMEOUT)
                        413 -> DataResult.Error(DataError.NETWORK_PAYLOAD_TOO_LARGE)
                        404 -> DataResult.Error(DataError.NETWORK_NOT_FOUND)
                        500 -> DataResult.Error(DataError.NETWORK_INTERNAL_SERVER_ERROR)
                        else -> DataResult.Error(DataError.UNKNOWN_NETWORK_ERROR)
                    }
                }

                is CancellationException -> {
                    // Don't catch cancellation exceptions
                    throw throwable
                }

                else ->
                    DataResult.Error(DataError.UNKNOWN_NETWORK_ERROR)
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    crashlytics: Crashlytics? = null,
    cacheCall: suspend () -> T
): DataResult<T, DataError> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                DataResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            crashlytics?.logException(throwable)
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    DataResult.Error(DataError.CACHE_ERROR_TIMEOUT)
                }

                is SQLiteConstraintException -> {
                    DataResult.Error(DataError.CONSTRAINT_VIOLATION)
                }

                is SQLiteException -> {
                    DataResult.Error(DataError.CACHE_ERROR)
                }

                is CancellationException -> {
                    // Don't catch cancellation exceptions
                    throw throwable
                }

                else -> {
                    DataResult.Error(DataError.UNKNOWN_DATABASE_ERROR)
                }
            }
        }
    }
}