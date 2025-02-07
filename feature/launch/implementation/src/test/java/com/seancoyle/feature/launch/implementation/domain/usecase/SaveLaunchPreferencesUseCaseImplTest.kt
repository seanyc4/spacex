package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.cache.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.usecase.SaveLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.SaveLaunchPreferencesUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveLaunchPreferencesUseCaseImplTest {

    @MockK(relaxed = true)
    private lateinit var launchPreferencesDataSource: LaunchPreferencesDataSource

    private lateinit var underTest: SaveLaunchPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = SaveLaunchPreferencesUseCaseImpl(launchPreferencesDataSource)
    }

    @Test
    fun `invoke should call saveLaunchPreferences with correct parameters`() = runTest {
        val order = Order.DESC
        val launchStatus = LaunchStatus.SUCCESS
        val launchYear = "2024"

        underTest.invoke(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )

        coVerify {
            launchPreferencesDataSource.saveLaunchPreferences(
                order = order,
                launchStatus = launchStatus,
                launchYear = launchYear
            )
        }
    }
}
