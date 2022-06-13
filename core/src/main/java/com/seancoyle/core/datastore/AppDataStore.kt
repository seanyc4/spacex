package com.seancoyle.core.datastore


interface AppDataStore {

    suspend fun setStringValue(
        key: String,
        value: String
    )

    suspend fun readStringValue(
        key: String,
    ): String?

    suspend fun setIntValue(
        key: String,
        value: Int
    )

    suspend fun readIntValue(
        key: String,
    ): Int?

    suspend fun clearData()

}