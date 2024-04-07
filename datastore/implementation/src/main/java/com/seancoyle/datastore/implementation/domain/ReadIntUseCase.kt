package com.seancoyle.datastore.implementation.domain

internal interface ReadIntUseCase {
    suspend fun invoke(key: String): Int?
}