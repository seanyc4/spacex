package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.api.domain.cache.LaunchPreferencesDataSource
import com.seancoyle.launch.api.domain.model.LaunchPrefs
import com.seancoyle.launch.api.domain.usecase.GetLaunchPreferencesUseCase
import javax.inject.Inject

internal class GetLaunchPreferencesUseCaseImpl @Inject constructor(
    private val launchPreferencesDataSource: LaunchPreferencesDataSource
) : GetLaunchPreferencesUseCase {
    override suspend fun invoke(): LaunchPrefs {
        return launchPreferencesDataSource.getLaunchPreferences()
    }
}