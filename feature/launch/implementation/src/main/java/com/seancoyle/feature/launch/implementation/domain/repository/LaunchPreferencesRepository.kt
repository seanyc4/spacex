package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus

internal interface LaunchPreferencesRepository {
    suspend fun getLaunchPreferences(): LaunchPrefs
    suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
}
