package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveLaunchesPreferencesUseCaseImplTest {

    @RelaxedMockK
    private lateinit var launchesPreferencesRepository: LaunchesPreferencesRepository

    private lateinit var underTest: SaveLaunchesPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = SaveLaunchesPreferencesUseCaseImpl(launchesPreferencesRepository)
    }

    @Test
    fun `invoke should call saveLaunchPreferences with correct parameters`() = runTest {
        val order = Order.DESC

        underTest.invoke(order)

        coVerify {
            launchesPreferencesRepository.saveLaunchPreferences(order)
        }
    }
}
