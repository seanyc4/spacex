package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import javax.inject.Inject

internal class LaunchPreferencesRepositoryImpl @Inject constructor(
    private val launchPreferencesDataSource: LaunchPreferencesDataSource
) : LaunchPreferencesRepository {

    override suspend fun saveLaunchPreferences(order: Order) {
        launchPreferencesDataSource.saveLaunchPreferences(order)
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return launchPreferencesDataSource.getLaunchPreferences()
    }
}
