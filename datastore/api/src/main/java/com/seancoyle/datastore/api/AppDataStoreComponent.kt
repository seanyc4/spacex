package com.seancoyle.datastore.api

interface AppDataStoreComponent {

    suspend fun saveStringUseCase(
        key: String,
        value: String
    )

    suspend fun readStringUseCase(
        key: String
    ): String?

    suspend fun saveIntUseCase(
        key: String,
        value: Int
    )

    suspend fun readIntUseCase(
        key: String
    ): Int?

    suspend fun clearDataUseCase()
}