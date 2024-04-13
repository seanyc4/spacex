package com.seancoyle.datastore.domain

internal interface ReadIntUseCase {
    suspend fun invoke(key: String): Int?
}