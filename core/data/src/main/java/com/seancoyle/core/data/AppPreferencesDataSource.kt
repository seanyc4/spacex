package com.seancoyle.core.data

interface AppPreferencesDataSource {

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