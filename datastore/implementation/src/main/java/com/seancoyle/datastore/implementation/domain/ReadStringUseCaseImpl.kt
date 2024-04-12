package com.seancoyle.datastore.implementation.domain

import com.seancoyle.datastore.api.AppDataStore
import javax.inject.Inject

internal class ReadStringUseCaseImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : ReadStringUseCase {
    override suspend fun invoke(key: String): String? {
        return appDataStore.readStringValue(key)
    }
}