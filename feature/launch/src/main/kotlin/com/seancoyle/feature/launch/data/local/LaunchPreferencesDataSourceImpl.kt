package com.seancoyle.feature.launch.data.local

import androidx.datastore.core.DataStore
import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.data.repository.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<LaunchPreferencesProto>,
    private val crashlytics: Crashlytics
) : LaunchPreferencesDataSource {

    override suspend fun saveLaunchPreferences(order: Order) {
        runSuspendCatching {
            dataStore.updateData { preferences ->
                preferences.toProto(order)
            }
        }.getOrElse { exception ->
            printLogDebug(this.javaClass.name, exception.message.toString())
            crashlytics.logException(exception)
        }
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return runSuspendCatching {
            dataStore.data.first().toModel()
        }.getOrElse { exception ->
            printLogDebug(this.javaClass.name, exception.message.toString())
            crashlytics.logException(exception)
            LaunchPrefs()
        }
    }
}
