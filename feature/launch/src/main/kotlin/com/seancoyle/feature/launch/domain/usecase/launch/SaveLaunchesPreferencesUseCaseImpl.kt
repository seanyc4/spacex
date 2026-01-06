package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import javax.inject.Inject

internal class SaveLaunchesPreferencesUseCaseImpl @Inject constructor(
    private val launchesPreferencesRepository: LaunchesPreferencesRepository
) : SaveLaunchesPreferencesUseCase {

    override suspend fun invoke(order: Order) {
        launchesPreferencesRepository.saveLaunchPreferences(order)
    }
}
