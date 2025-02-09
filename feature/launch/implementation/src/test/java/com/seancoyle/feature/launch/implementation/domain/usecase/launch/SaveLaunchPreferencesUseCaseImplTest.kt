package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveLaunchPreferencesUseCaseImplTest {

    @RelaxedMockK
    private lateinit var launchPreferencesRepository: LaunchPreferencesRepository

    private lateinit var underTest: SaveLaunchPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = SaveLaunchPreferencesUseCaseImpl(launchPreferencesRepository)
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
            launchPreferencesRepository.saveLaunchPreferences(
                order = order,
                launchStatus = launchStatus,
                launchYear = launchYear
            )
        }
    }
}
