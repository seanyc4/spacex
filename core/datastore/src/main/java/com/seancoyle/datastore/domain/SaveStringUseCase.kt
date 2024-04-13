package com.seancoyle.datastore.domain

internal interface SaveStringUseCase {
    suspend fun invoke(key: String, value: String)
}