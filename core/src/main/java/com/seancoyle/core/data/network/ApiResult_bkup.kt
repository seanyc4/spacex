package com.seancoyle.core.data.network

sealed class ApiResult_bkup<out T> {

    data class Success<out T>(val value: T): ApiResult_bkup<T>()

    data class Error(
        val code: Int? = null,
        val errorMessage: String? = null
    ): ApiResult_bkup<Nothing>()

    object NetworkError: ApiResult_bkup<Nothing>()
}