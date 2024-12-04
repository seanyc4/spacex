package com.seancoyle.core.datastore.implementation.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.core.datastore.implementation.domain.DataStorePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DataStorePreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val crashlyticsWrapper: Crashlytics
) : DataStorePreferences {

    override suspend fun getString(
        key: String,
        defaultValue: String
    ): String {
        return runCatching {
            dataStore.data.first()[stringPreferencesKey(key)] ?: defaultValue
        }.getOrElse { exception ->
            logException("Error getting string value for key: $key", exception)
            defaultValue
        }
    }

    override fun observeString(key: String, defaultValue: String): Flow<String> {
        val dataStoreKey = stringPreferencesKey(key)
        return dataStore.data.map {
            it[dataStoreKey] ?: defaultValue
        }.catch { exception ->
            logException("Error observing string value for key: $key", exception)
            emit(defaultValue)
        }
    }

    override suspend fun getInt(
        key: String,
        defaultValue: Int
    ): Int {
        return runCatching {
            dataStore.data.first()[intPreferencesKey(key)] ?: defaultValue
        }.getOrElse { exception ->
            logException("Error getting int value for key: $key", exception)
            defaultValue
        }
    }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
        val dataStoreKey = intPreferencesKey(key)
        return dataStore.data.map {
            it[dataStoreKey] ?: defaultValue
        }.catch { exception ->
            logException("Error observing int value for key: $key", exception)
            emit(defaultValue)
        }
    }

    override suspend fun getLong(
        key: String,
        defaultValue: Long
    ): Long {
        return runCatching {
            dataStore.data.first()[longPreferencesKey(key)] ?: defaultValue
        }.getOrElse { exception ->
            logException("Error getting long value for key: $key", exception)
            defaultValue
        }
    }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
        val dataStoreKey = longPreferencesKey(key)
        return dataStore.data.map {
            it[dataStoreKey] ?: defaultValue
        }.catch { exception ->
            logException("Error observing long value for key: $key", exception)
            emit(defaultValue)
        }
    }

    override suspend fun getFloat(
        key: String,
        defaultValue: Float
    ): Float {
        return runCatching {
            dataStore.data.first()[floatPreferencesKey(key)] ?: defaultValue
        }.getOrElse { exception ->
            logException("Error getting float value for key: $key", exception)
            defaultValue
        }
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
        val dataStoreKey = floatPreferencesKey(key)
        return dataStore.data.map {
            it[dataStoreKey] ?: defaultValue
        }.catch { exception ->
            logException("Error observing float value for key: $key", exception)
            emit(defaultValue)
        }
    }

    override suspend fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean {
        return runCatching {
            dataStore.data.first()[booleanPreferencesKey(key)] ?: defaultValue
        }.getOrElse { exception ->
            logException("Error getting boolean value for key: $key", exception)
            defaultValue
        }
    }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[dataStoreKey] ?: defaultValue
        }.catch { exception ->
            logException("Error observing boolean value for key: $key", exception)
            emit(defaultValue)
        }
    }

    override suspend fun contains(key: String): Boolean {
        val preferences = dataStore.data.first()
        return runCatching {
            preferences.asMap().containsKey(stringPreferencesKey(key))
        }.getOrElse { exception ->
            logException("Error checking if key exists: $key", exception)
            false
        }
    }

    override suspend fun delete(key: String) {
        runCatching {
            dataStore.edit { it.remove(stringPreferencesKey(key)) }
        }.onFailure { exception ->
            logException("Error deleting key: $key", exception)
        }
    }

    override suspend fun saveString(
        key: String,
        value: String
    ) {
        runCatching {
            dataStore.edit { it[stringPreferencesKey(key)] = value }
        }.onFailure { exception ->
            logException("Error saving string value for key: $key", exception)
        }
    }

    override suspend fun saveInt(
        key: String,
        value: Int
    ) {
        runCatching {
            dataStore.edit { it[intPreferencesKey(key)] = value }
        }.onFailure { exception ->
            logException("Error saving int value for key: $key", exception)
        }
    }

    override suspend fun saveLong(
        key: String,
        value: Long
    ) {
        runCatching {
            dataStore.edit { it[longPreferencesKey(key)] = value }
        }.onFailure { exception ->
            logException("Error saving long value for key: $key", exception)
        }
    }

    override suspend fun saveFloat(
        key: String,
        value: Float
    ) {
        runCatching {
            dataStore.edit { it[floatPreferencesKey(key)] = value }
        }.onFailure { exception ->
            logException("Error saving float value for key: $key", exception)
        }
    }

    override suspend fun saveBoolean(
        key: String,
        value: Boolean
    ) {
        runCatching {
            dataStore.edit { it[booleanPreferencesKey(key)] = value }
        }.onFailure { exception ->
            logException("Error saving boolean value for key: $key", exception)
        }
    }

    private fun logException(message: String, exception: Throwable) {
        printLogDebug(message, exception.message.orEmpty())
        crashlyticsWrapper.logException(exception)
    }
}