package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs

interface GetLaunchPreferencesUseCase {
    suspend operator fun invoke(): LaunchPrefs
}