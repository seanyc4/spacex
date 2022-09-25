package com.seancoyle.core_datastore_test

import com.seancoyle.core_datastore.AppDataStore

class AppDataStoreManagerFake: AppDataStore {

    private val datastore: MutableMap<String, Any> = mutableMapOf()

    override suspend fun setStringValue(key: String, value: String) {
        datastore[key] = value
    }

    override suspend fun readStringValue(key: String): String {
        return datastore[key].toString()
    }

    override suspend fun setIntValue(key: String, value: Int) {
        datastore[key] = value
    }

    override suspend fun readIntValue(key: String): Int {
        return datastore[key].hashCode()
    }

    override suspend fun clearData() {
        datastore.clear()
    }

}