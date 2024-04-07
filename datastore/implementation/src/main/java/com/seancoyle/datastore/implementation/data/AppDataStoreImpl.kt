package com.seancoyle.datastore.implementation.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.seancoyle.datastore.implementation.domain.AppDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val APP_DATASTORE = "app"
internal class AppDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppDataStore {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(APP_DATASTORE)
    }

    override suspend fun saveStringValue(
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

    override suspend fun saveIntValue(
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