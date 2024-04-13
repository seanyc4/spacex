package com.seancoyle.datastore.domain

internal interface ClearDataUseCase {
    suspend fun invoke()
}