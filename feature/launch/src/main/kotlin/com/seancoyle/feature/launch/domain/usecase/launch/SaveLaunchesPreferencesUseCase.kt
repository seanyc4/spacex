package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.domain.Order

interface SaveLaunchesPreferencesUseCase {
    suspend operator fun invoke(order: Order)
}
