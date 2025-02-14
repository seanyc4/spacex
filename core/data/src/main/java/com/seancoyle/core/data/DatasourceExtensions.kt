package com.seancoyle.core.data

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
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
): LaunchResult<T, DataSourceError> {
    return withContext(dispatcher) {
        try {
            withTimeout(NETWORK_TIMEOUT) {
                LaunchResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            crashlytics?.logException(throwable)
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    LaunchResult.Error(DataSourceError.NETWORK_TIMEOUT)
                }

                is IOException -> {
                    LaunchResult.Error(DataSourceError.NETWORK_CONNECTION_FAILED)
                }

                is HttpException -> {
                    when (throwable.code()) {
                        401 -> LaunchResult.Error(DataSourceError.NETWORK_UNAUTHORIZED)
                        403 -> LaunchResult.Error(DataSourceError.NETWORK_FORBIDDEN)
                        404 -> LaunchResult.Error(DataSourceError.NETWORK_NOT_FOUND)
                        408 -> LaunchResult.Error(DataSourceError.NETWORK_TIMEOUT)
                        413 -> LaunchResult.Error(DataSourceError.NETWORK_PAYLOAD_TOO_LARGE)
                        500 -> LaunchResult.Error(DataSourceError.NETWORK_INTERNAL_SERVER_ERROR)
                        else -> LaunchResult.Error(DataSourceError.NETWORK_UNKNOWN_ERROR)
                    }
                }

                is CancellationException -> {
                    // Don't catch cancellation exceptions
                    throw throwable
                }

                else ->
                    LaunchResult.Error(DataSourceError.NETWORK_UNKNOWN_ERROR)
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    crashlytics: Crashlytics? = null,
    cacheCall: suspend () -> T
): LaunchResult<T, DataSourceError> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                LaunchResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            crashlytics?.logException(throwable)
            throwable.printStackTrace()
            when (throwable) {
                is TimeoutCancellationException -> {
                    LaunchResult.Error(DataSourceError.CACHE_ERROR_TIMEOUT)
                }

                is SQLiteConstraintException -> {
                    LaunchResult.Error(DataSourceError.CACHE_CONSTRAINT_VIOLATION)
                }

                is SQLiteException -> {
                    LaunchResult.Error(DataSourceError.CACHE_ERROR)
                }

                is CancellationException -> {
                    // Don't catch cancellation exceptions
                    throw throwable
                }

                else -> {
                    LaunchResult.Error(DataSourceError.CACHE_UNKNOWN_DATABASE_ERROR)
                }
            }
        }
    }
}