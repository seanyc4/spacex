package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetLaunchesPreferencesUseCaseImplTest {

    @MockK
    private lateinit var launchesPreferencesRepository: LaunchesPreferencesRepository

    private lateinit var underTest: GetLaunchesPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchesPreferencesUseCaseImpl(launchesPreferencesRepository)
    }

    @Test
    fun `invoke should return launch preferences when data source succeeds`() = runTest {
        val launchPrefs = LaunchPrefs(
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            query = "2024"
        )
        coEvery { launchesPreferencesRepository.getLaunchPreferences() } returns launchPrefs

        val result = underTest.invoke()

        coVerify { launchesPreferencesRepository.getLaunchPreferences() }

        assertEquals(launchPrefs, result)
    }
}