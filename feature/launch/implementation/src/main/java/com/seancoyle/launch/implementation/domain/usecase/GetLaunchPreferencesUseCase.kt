package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.api.domain.model.LaunchPrefs

interface GetLaunchPreferencesUseCase {
    suspend operator fun invoke(): LaunchPrefs
}