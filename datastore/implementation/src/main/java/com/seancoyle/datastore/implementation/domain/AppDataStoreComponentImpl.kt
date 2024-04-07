package com.seancoyle.datastore.implementation.domain

import com.seancoyle.datastore.api.AppDataStoreComponent
import javax.inject.Inject

internal class AppDataStoreComponentImpl @Inject constructor(
    private val clearDataUseCase: ClearDataUseCase,
    private val readIntUseCase: ReadIntUseCase,
    private val readStringUseCase: ReadStringUseCase,
    private val saveIntUseCase: SaveIntUseCase,
    private val saveStringUseCase: SaveStringUseCase
): AppDataStoreComponent {
    override suspend fun saveStringUseCase(key: String, value: String) {
        saveStringUseCase.invoke(key, value)
    }

    override suspend fun readStringUseCase(key: String): String? {
        return readStringUseCase.invoke(key)
    }

    override suspend fun saveIntUseCase(key: String, value: Int) {
        saveIntUseCase.invoke(key, value)
    }

    override suspend fun readIntUseCase(key: String): Int? {
        return readIntUseCase.invoke(key)
    }

    override suspend fun clearDataUseCase() {
        clearDataUseCase.invoke()
    }
}