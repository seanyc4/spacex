package com.seancoyle.core.common.result

sealed interface Result<out T, out E> {
    data class Success<out T>(val data: T) : Result<T, Nothing>
    data class Error<out E : ErrorType>(val error: E) : Result<Nothing, E>
}