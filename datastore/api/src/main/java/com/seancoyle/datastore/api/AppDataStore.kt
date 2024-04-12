package com.seancoyle.datastore.api

interface AppDataStore {

    suspend fun saveStringValue(
        key: String,
        value: String
    )

    suspend fun readStringValue(
        key: String,
    ): String?

    suspend fun saveIntValue(
        key: String,
        value: Int
    )

    suspend fun readIntValue(
        key: String,
    ): Int?

    suspend fun clearData()

}