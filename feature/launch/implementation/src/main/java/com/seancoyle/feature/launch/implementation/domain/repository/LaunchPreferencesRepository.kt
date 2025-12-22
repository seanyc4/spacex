package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.model.LaunchPrefs

internal interface LaunchPreferencesRepository {
    suspend fun getLaunchPreferences(): LaunchPrefs
    suspend fun saveLaunchPreferences(order: Order)
}
