package com.seancoyle.datastore.implementation.domain

internal interface SaveIntUseCase {
    suspend fun invoke(key: String, value: Int)
}