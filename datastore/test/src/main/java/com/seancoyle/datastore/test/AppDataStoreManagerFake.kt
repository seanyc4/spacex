package com.seancoyle.datastore.test

import com.seancoyle.datastore.api.AppDataStore

class AppDataStoreManagerFake : AppDataStore {

    companion object {
        private val datastore: MutableMap<String, Any> = mutableMapOf()
    }

    override suspend fun saveStringValue(key: String, value: String) {
        datastore[key] = value
    }

    override suspend fun readStringValue(key: String): String? {
        return datastore[key]?.toString()
    }

    override suspend fun saveIntValue(key: String, value: Int) {
        datastore[key] = value
    }

    override suspend fun readIntValue(key: String): Int? {
        return datastore[key]?.hashCode()
    }

    override suspend fun clearData() {
        datastore.clear()
    }

}