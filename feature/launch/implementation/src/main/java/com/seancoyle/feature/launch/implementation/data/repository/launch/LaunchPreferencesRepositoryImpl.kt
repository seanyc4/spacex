package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import javax.inject.Inject

internal class LaunchPreferencesRepositoryImpl @Inject constructor(
    private val launchPreferencesDataSource: LaunchPreferencesDataSource
) : LaunchPreferencesRepository {

    override suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        launchPreferencesDataSource.saveLaunchPreferences(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return launchPreferencesDataSource.getLaunchPreferences()
    }
}
