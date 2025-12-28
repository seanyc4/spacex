package com.seancoyle.feature.launch.domain.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs

internal interface LaunchPreferencesRepository {
    suspend fun getLaunchPreferences(): LaunchPrefs
    suspend fun saveLaunchPreferences(order: Order)
}
