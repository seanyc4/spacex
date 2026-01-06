package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import javax.inject.Inject

internal class LaunchesPreferencesRepositoryImpl @Inject constructor(
    private val launchesPreferencesDataSource: LaunchesPreferencesDataSource
) : LaunchesPreferencesRepository {

    override suspend fun saveLaunchPreferences(order: Order) {
        launchesPreferencesDataSource.saveLaunchPreferences(order)
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return launchesPreferencesDataSource.getLaunchPreferences()
    }
}
