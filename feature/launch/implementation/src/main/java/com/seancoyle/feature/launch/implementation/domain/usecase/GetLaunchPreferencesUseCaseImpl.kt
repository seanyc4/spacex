package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import javax.inject.Inject

internal class GetLaunchPreferencesUseCaseImpl @Inject constructor(
    private val spaceXRepository: SpaceXRepository
) : GetLaunchPreferencesUseCase {

    override suspend fun invoke(): LaunchPrefs {
        return spaceXRepository.getLaunchPreferences()
    }
}