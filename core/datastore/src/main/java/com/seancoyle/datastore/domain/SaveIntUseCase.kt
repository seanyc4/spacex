package com.seancoyle.datastore.domain

internal interface SaveIntUseCase {
    suspend fun invoke(key: String, value: Int)
}