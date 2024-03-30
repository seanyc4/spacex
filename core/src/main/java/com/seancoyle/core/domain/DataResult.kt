package com.seancoyle.core.domain


sealed interface DataResult<out T, out E : ErrorType> {
    data object Loading : DataResult<Nothing, Nothing>
    data class Success<out T>(val data: T) : DataResult<T, Nothing>
    data class Error<out E : ErrorType>(val error: E) : DataResult<Nothing, E>
}