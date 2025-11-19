package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus

interface SaveLaunchPreferencesUseCase {
    suspend operator fun invoke(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
}
