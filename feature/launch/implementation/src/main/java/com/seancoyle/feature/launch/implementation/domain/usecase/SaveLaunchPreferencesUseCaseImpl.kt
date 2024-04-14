package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.cache.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import javax.inject.Inject

internal class SaveLaunchPreferencesUseCaseImpl @Inject constructor(
    private val launchPreferencesDataSource: LaunchPreferencesDataSource
) : SaveLaunchPreferencesUseCase {
    override suspend fun invoke(
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
}