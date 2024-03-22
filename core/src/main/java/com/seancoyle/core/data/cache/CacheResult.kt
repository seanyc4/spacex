package com.seancoyle.core.data.cache

import com.seancoyle.core.data.cache.CacheErrors.CACHE_ERROR_UNKNOWN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface CacheResult<out T> {
    data class Success<T>(val data: T) : CacheResult<T>
    data class Error(val exception: String) : CacheResult<Nothing>
    data object Loading : CacheResult<Nothing>
}

fun <T> Flow<T>.cacheResult(): Flow<CacheResult<T>> {
    return this
        .map<T, CacheResult<T>> {
            CacheResult.Success(it)
        }
        .onStart { emit(CacheResult.Loading) }
        .catch { throwable ->
            emit(CacheResult.Error(throwable.message ?: CACHE_ERROR_UNKNOWN))
        }
}