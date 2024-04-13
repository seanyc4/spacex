package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.LaunchPrefs

interface GetLaunchPreferencesUseCase {
    suspend operator fun invoke(): LaunchPrefs
}