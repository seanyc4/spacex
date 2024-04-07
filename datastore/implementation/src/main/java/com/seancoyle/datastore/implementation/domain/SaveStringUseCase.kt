package com.seancoyle.datastore.implementation.domain

internal interface SaveStringUseCase {
    suspend fun invoke(key: String, value: String)
}