package com.seancoyle.core.datastore.implementation.domain

import com.seancoyle.core.datastore.api.DataStorePreferencesComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DataStorePreferencesComponentImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
) : DataStorePreferencesComponent {

    override suspend fun getString(key: String, defaultValue: String): String {
        return dataStorePreferences.getString(key, defaultValue)
    }

    override fun observeString(key: String, defaultValue: String): Flow<String> {
        return dataStorePreferences.observeString(key, defaultValue)
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return dataStorePreferences.getInt(key, defaultValue)
    }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
        return dataStorePreferences.observeInt(key, defaultValue)
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return dataStorePreferences.getLong(key, defaultValue)
    }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
        return dataStorePreferences.observeLong(key, defaultValue)
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return dataStorePreferences.getFloat(key, defaultValue)
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
        return dataStorePreferences.observeFloat(key, defaultValue)
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return dataStorePreferences.getBoolean(key, defaultValue)
    }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return dataStorePreferences.observeBoolean(key, defaultValue)
    }

    override suspend fun contains(key: String): Boolean {
        return dataStorePreferences.contains(key)
    }

    override suspend fun delete(key: String) {
        dataStorePreferences.delete(key)
    }

    override suspend fun saveString(key: String, value: String) {
        dataStorePreferences.saveString(key, value)
    }

    override suspend fun saveInt(key: String, value: Int) {
        dataStorePreferences.saveInt(key, value)
    }

    override suspend fun saveLong(key: String, value: Long) {
        dataStorePreferences.saveLong(key, value)
    }

    override suspend fun saveFloat(key: String, value: Float) {
        dataStorePreferences.saveFloat(key, value)
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        dataStorePreferences.saveBoolean(key, value)
    }
}