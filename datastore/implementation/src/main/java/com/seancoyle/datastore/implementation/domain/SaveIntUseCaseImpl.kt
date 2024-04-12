package com.seancoyle.datastore.implementation.domain

import com.seancoyle.datastore.api.AppDataStore
import javax.inject.Inject

internal class SaveIntUseCaseImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : SaveIntUseCase {
    override suspend fun invoke(key: String, value: Int) {
        appDataStore.saveIntValue(key, value)
    }
}