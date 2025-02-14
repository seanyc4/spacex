package com.seancoyle.core.common.result

sealed interface LaunchResult<out T, out E> {
    data class Success<out T>(val data: T) : LaunchResult<T, Nothing>
    data class Error<out E : ErrorType>(val error: E, val errorDescription: String? = null) : LaunchResult<Nothing, E>
}
