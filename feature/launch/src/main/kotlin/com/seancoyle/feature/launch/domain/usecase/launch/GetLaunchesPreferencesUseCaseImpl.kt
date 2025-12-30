package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import javax.inject.Inject

internal class GetLaunchesPreferencesUseCaseImpl @Inject constructor(
    private val launchesPreferencesRepository: LaunchesPreferencesRepository
) : GetLaunchesPreferencesUseCase {

    override suspend fun invoke(): LaunchPrefs {
        return launchesPreferencesRepository.getLaunchPreferences()
    }
}
