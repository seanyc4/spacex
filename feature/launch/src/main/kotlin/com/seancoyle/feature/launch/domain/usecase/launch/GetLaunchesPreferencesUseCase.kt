package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.feature.launch.domain.model.LaunchPrefs

interface GetLaunchesPreferencesUseCase {
    suspend operator fun invoke(): LaunchPrefs
}
