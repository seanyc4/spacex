package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchStatus

interface SaveLaunchPreferencesUseCase {
    suspend operator fun invoke(
        order: Order,
        launchStatus: LaunchStatus,
        year: String
    )
}