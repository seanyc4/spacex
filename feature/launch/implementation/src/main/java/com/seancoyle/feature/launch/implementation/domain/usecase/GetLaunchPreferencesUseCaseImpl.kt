package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.feature.launch.implementation.domain.cache.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import javax.inject.Inject

internal class GetLaunchPreferencesUseCaseImpl @Inject constructor(
    private val launchPreferencesDataSource: LaunchPreferencesDataSource
) : GetLaunchPreferencesUseCase {
    override suspend fun invoke(): LaunchPrefs {
        return launchPreferencesDataSource.getLaunchPreferences()
    }
}