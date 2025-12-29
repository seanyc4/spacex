package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs

interface LaunchPreferencesDataSource {
    suspend fun saveLaunchPreferences(order: Order)
    suspend fun getLaunchPreferences(): LaunchPrefs
}
