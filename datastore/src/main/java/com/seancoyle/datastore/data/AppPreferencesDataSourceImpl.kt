package com.seancoyle.datastore.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.seancoyle.datastore.domain.AppPreferencesDataSource
import kotlinx.coroutines.flow.first

internal class AppPreferencesDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : AppPreferencesDataSource {

    override suspend fun saveStringValue(
        key: String,
        value: String
    ) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun readStringValue(key: String): String? {
        return dataStore.data.first()[stringPreferencesKey(key)]
    }

    override suspend fun saveIntValue(
        key: String,
        value: Int
    ) {
        dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    override suspend fun readIntValue(key: String): Int? {
        return dataStore.data.first()[intPreferencesKey(key)]
    }

    override suspend fun clearData() {
        dataStore.edit { it.clear() }
    }
}