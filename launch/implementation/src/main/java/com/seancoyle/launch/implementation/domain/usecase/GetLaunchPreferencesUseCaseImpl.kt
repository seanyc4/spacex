package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.api.domain.cache.LaunchPreferencesDataSource
import com.seancoyle.launch.api.domain.model.LaunchPreferencesTest
import com.seancoyle.launch.api.domain.usecase.GetLaunchPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetLaunchPreferencesUseCaseImpl @Inject constructor(
    private val launchPreferencesDataSource: LaunchPreferencesDataSource
) : GetLaunchPreferencesUseCase {
    override suspend fun invoke(): Flow<LaunchPreferencesTest> {
        return launchPreferencesDataSource.getLaunchPreferences()
    }
}