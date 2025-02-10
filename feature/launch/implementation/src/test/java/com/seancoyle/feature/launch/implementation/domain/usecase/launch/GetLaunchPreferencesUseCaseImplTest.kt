package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetLaunchPreferencesUseCaseImplTest {

    @MockK
    private lateinit var launchPreferencesRepository: LaunchPreferencesRepository

    private lateinit var underTest: GetLaunchPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchPreferencesUseCaseImpl(launchPreferencesRepository)
    }

    @Test
    fun `invoke should return launch preferences when data source succeeds`() = runTest {
        val launchPrefs = LaunchPrefs(
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            launchYear = "2024"
        )
        coEvery { launchPreferencesRepository.getLaunchPreferences() } returns launchPrefs

        val result = underTest.invoke()

        coVerify { launchPreferencesRepository.getLaunchPreferences() }

        assertEquals(launchPrefs, result)
    }
}