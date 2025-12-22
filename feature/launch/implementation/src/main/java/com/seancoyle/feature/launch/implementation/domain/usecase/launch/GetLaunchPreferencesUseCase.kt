package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.feature.launch.implementation.domain.model.LaunchPrefs

interface GetLaunchPreferencesUseCase {
    suspend operator fun invoke(): LaunchPrefs
}
