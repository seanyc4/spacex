package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import javax.inject.Inject

internal class SaveLaunchPreferencesUseCaseImpl @Inject constructor(
    private val spaceXRepository: SpaceXRepository
) : SaveLaunchPreferencesUseCase {

    override suspend fun invoke(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        spaceXRepository.saveLaunchPreferences(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }
}