package com.seancoyle.core.common.result

typealias RootError = Error

sealed interface LaunchResult<out T, out E> {
    data class Success<out T>(val data: T) : LaunchResult<T, Nothing>
    data class Error<out E : Throwable>(val error: E, val errorDescription: String? = null) : LaunchResult<Nothing, E>
}
