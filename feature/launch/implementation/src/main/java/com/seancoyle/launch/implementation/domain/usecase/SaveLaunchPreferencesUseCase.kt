package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchStatus

interface SaveLaunchPreferencesUseCase {
    suspend operator fun invoke(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
}