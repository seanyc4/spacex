package com.seancoyle.core.datastore.implementation.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.rules.TemporaryFolder
import java.io.IOException

class FakeDataStorePreferences : DataStore<Preferences> {
    override val data: Flow<Preferences> = flow {
        throw IOException("error in data fetching")
    }

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        throw IOException("error in edit")
    }
}

fun fakeDataStore(
    scope: CoroutineScope,
    tmpFolder: TemporaryFolder,
    throwException: Boolean = false
): DataStore<Preferences> {
    return if (throwException) {
        FakeDataStorePreferences()
    } else {
        tmpFolder.testDataStorePreferences(scope)
    }
}

fun TemporaryFolder.testDataStorePreferences(
    coroutineScope: CoroutineScope,
) = PreferenceDataStoreFactory.create(
    scope = coroutineScope,
) {
    newFile("datastore_preferences_test.preferences_pb")
}