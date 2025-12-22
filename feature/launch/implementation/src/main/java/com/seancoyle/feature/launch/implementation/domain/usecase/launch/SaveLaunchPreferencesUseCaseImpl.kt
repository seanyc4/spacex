package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import javax.inject.Inject

internal class SaveLaunchPreferencesUseCaseImpl @Inject constructor(
    private val launchPreferencesRepository: LaunchPreferencesRepository
) : SaveLaunchPreferencesUseCase {

    override suspend fun invoke(order: Order) {
        launchPreferencesRepository.saveLaunchPreferences(order)
    }
}
