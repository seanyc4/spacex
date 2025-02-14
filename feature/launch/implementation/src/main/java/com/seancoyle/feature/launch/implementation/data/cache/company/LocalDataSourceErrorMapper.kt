package com.seancoyle.feature.launch.implementation.data.cache.company

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.seancoyle.core.common.result.DataSourceError
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class LocalDataSourceErrorMapper @Inject constructor() {
    fun map(throwable: Throwable?): DataSourceError {
        return when (throwable) {
            is TimeoutCancellationException -> DataSourceError.CACHE_ERROR_TIMEOUT
            is SQLiteConstraintException -> DataSourceError.CACHE_CONSTRAINT_VIOLATION
            is SQLiteException -> DataSourceError.CACHE_ERROR
            is CancellationException -> throw throwable
            else -> DataSourceError.CACHE_UNKNOWN_DATABASE_ERROR
        }
    }
}