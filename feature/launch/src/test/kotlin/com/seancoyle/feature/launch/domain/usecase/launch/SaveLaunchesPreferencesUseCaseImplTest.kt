package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveLaunchesPreferencesUseCaseImplTest {

    @MockK
    private lateinit var repository: LaunchesPreferencesRepository

    private lateinit var underTest: SaveLaunchesPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = SaveLaunchesPreferencesUseCaseImpl(repository)
    }

    @Test
    fun `GIVEN ASC order WHEN invoke THEN saves order to repository`() = runTest {
        val order = Order.ASC
        coJustRun { repository.saveLaunchPreferences(order) }

        underTest.invoke(order)

        coVerify { repository.saveLaunchPreferences(order) }
    }

    @Test
    fun `GIVEN DESC order WHEN invoke THEN saves order to repository`() = runTest {
        val order = Order.DESC
        coJustRun { repository.saveLaunchPreferences(order) }

        underTest.invoke(order)

        coVerify { repository.saveLaunchPreferences(Order.DESC) }
    }
}
