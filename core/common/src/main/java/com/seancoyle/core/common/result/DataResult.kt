package com.seancoyle.core.common.result

sealed interface DataResult<out T, out E> {
    data class Success<out T>(val data: T) : DataResult<T, Nothing>
    data class Error<out E : ErrorType>(val error: E) : DataResult<Nothing, E>
}