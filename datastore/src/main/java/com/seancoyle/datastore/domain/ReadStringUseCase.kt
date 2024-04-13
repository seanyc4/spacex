package com.seancoyle.datastore.domain

internal interface ReadStringUseCase {
    suspend fun invoke(key: String): String?
}