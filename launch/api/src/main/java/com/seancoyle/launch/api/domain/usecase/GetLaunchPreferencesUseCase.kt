package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.LaunchPreferencesTest
import kotlinx.coroutines.flow.Flow

interface GetLaunchPreferencesUseCase {
    suspend operator fun invoke(): Flow<LaunchPreferencesTest>
}