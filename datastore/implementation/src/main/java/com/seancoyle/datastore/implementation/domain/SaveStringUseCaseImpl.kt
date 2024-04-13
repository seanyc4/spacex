package com.seancoyle.datastore.implementation.domain

import javax.inject.Inject

internal class SaveStringUseCaseImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : SaveStringUseCase {
    override suspend fun invoke(key: String, value: String) {
        appDataStore.saveStringValue(key, value)
    }
}