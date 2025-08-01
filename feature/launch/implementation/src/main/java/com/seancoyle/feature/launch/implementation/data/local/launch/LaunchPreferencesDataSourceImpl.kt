package com.seancoyle.feature.launch.implementation.data.local.launch

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.core.datastore_proto.LaunchPreferencesProto
import com.seancoyle.core.datastore_proto.LaunchStatusProto
import com.seancoyle.core.datastore_proto.OrderProto
import com.seancoyle.core.datastore_proto.copy
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchPreferencesDataSource
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<LaunchPreferencesProto>,
    private val crashlytics: Crashlytics
) : LaunchPreferencesDataSource {

    override suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        try {
            dataStore.updateData { preferences ->
                preferences.copy {
                    this.order = OrderProto.valueOf(order.name)
                    this.launchStatus = LaunchStatusProto.valueOf(launchStatus.name)
                    this.launchDate = launchYear
                }
            }
        } catch (ioException: IOException) {
            printLogDebug(this.javaClass.name, ioException.message.toString())
            crashlytics.logException(ioException)
        }
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        val preferences = dataStore.data.first()
        return preferences.toModel()
    }

    private fun LaunchPreferencesProto.toModel(): LaunchPrefs = LaunchPrefs(
        order = Order.valueOf(this.order.name),
        launchStatus = LaunchStatus.valueOf(this.launchStatus.name),
        launchYear = this.launchDate
    )

}
