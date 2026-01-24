package com.seancoyle.feature.launch.data.local

import androidx.datastore.core.DataStore
import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.data.repository.LaunchesPreferencesDataSource
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchesPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<LaunchPreferencesProto>,
    private val crashLogger: CrashLogger
) : LaunchesPreferencesDataSource {

    override suspend fun saveLaunchPreferences(order: Order) {
        runSuspendCatching {
            dataStore.updateData { preferences ->
                preferences.toProto(order)
            }
        }.getOrElse { exception ->
            printLogDebug(this.javaClass.name, exception.message.toString())
            crashLogger.logException(exception)
        }
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return runSuspendCatching {
            dataStore.data.first().toModel()
        }.getOrElse { exception ->
            printLogDebug(this.javaClass.name, exception.message.toString())
            crashLogger.logException(exception)
            LaunchPrefs()
        }
    }
}
