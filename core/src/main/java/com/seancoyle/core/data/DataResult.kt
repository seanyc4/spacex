package com.seancoyle.core.data

import com.seancoyle.core.data.DataResult.Companion.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val exception: String) : DataResult<Nothing>
    data object Loading : DataResult<Nothing>

    companion object {
        const val UNKNOWN_ERROR = "Unknown error"
    }
}

fun <T> Flow<T>.asResult(): Flow<DataResult<T>> {
    return this
        .map<T, DataResult<T>> {
            DataResult.Success(it)
        }
        .onStart { emit(DataResult.Loading) }
        .catch { exception ->
            emit(DataResult.Error(exception.message ?: UNKNOWN_ERROR)) }
}