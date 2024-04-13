package com.seancoyle.launch.api.domain.cache

import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchPreferencesTest
import com.seancoyle.launch.api.domain.model.LaunchStatus
import kotlinx.coroutines.flow.Flow

interface LaunchPreferencesDataSource {
    suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    )
    suspend fun getLaunchPreferences(): Flow<LaunchPreferencesTest>
}