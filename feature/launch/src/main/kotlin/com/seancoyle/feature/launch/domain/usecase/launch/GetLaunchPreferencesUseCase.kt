package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.feature.launch.domain.model.LaunchPrefs

interface GetLaunchPreferencesUseCase {
    suspend operator fun invoke(): LaunchPrefs
}
