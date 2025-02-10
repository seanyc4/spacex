package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import javax.inject.Inject

internal class GetLaunchPreferencesUseCaseImpl @Inject constructor(
    private val launchPreferencesRepository: LaunchPreferencesRepository
) : GetLaunchPreferencesUseCase {

    override suspend fun invoke(): LaunchPrefs {
        return launchPreferencesRepository.getLaunchPreferences()
    }
}