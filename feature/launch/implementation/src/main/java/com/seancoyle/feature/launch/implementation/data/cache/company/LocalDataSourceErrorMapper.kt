package com.seancoyle.feature.launch.implementation.data.cache.company

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.result.DataError.LocalError
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class LocalDataSourceErrorMapper @Inject constructor() {
    fun map(throwable: Throwable): LocalError {
        return when (throwable) {
            is TimeoutCancellationException -> LocalError.CACHE_ERROR_TIMEOUT
            is SQLiteConstraintException -> LocalError.CACHE_CONSTRAINT_VIOLATION
            is SQLiteException -> LocalError.CACHE_ERROR
            is CancellationException -> throw throwable
            else -> LocalError.CACHE_UNKNOWN_DATABASE_ERROR
        }
    }
}