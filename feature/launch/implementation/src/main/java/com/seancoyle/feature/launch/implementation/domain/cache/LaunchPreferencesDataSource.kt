package com.seancoyle.feature.launch.implementation.domain.cache

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus

interface LaunchPreferencesDataSource {
    suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
    suspend fun getLaunchPreferences(): LaunchPrefs
}