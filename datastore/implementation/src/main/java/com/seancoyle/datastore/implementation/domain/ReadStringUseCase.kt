package com.seancoyle.datastore.implementation.domain

internal interface ReadStringUseCase {
    suspend fun invoke(key: String): String?
}