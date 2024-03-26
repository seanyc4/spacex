package com.seancoyle.core_datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val APP_DATASTORE = "app"

internal class AppDataStoreManager(
    val context: Application
) : AppDataStore {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(APP_DATASTORE)
    }

    override suspend fun setStringValue(
        key: String,
        value: String
    ) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun readStringValue(key: String): String? {
        return context.dataStore.data.first()[stringPreferencesKey(key)]
    }

    override suspend fun setIntValue(
        key: String,
        value: Int
    ) {
        context.dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    override suspend fun readIntValue(key: String): Int? {
        return context.dataStore.data.first()[intPreferencesKey(key)]
    }

    override suspend fun clearData() {
        context.dataStore.edit { it.clear() }
    }
}