package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
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

        underTest.invoke(order)

        coVerify {
            launchPreferencesRepository.saveLaunchPreferences(order)
        }
    }
}
