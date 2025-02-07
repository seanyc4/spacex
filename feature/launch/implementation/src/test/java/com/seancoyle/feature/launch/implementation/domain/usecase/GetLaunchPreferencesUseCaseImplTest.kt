package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import com.seancoyle.feature.launch.implementation.domain.usecase.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetLaunchPreferencesUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetLaunchPreferencesUseCaseImplTest {

    @MockK
    private lateinit var spaceXRepository: SpaceXRepository

    private lateinit var underTest: GetLaunchPreferencesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchPreferencesUseCaseImpl(spaceXRepository)
    }

    @Test
    fun `invoke should return launch preferences when data source succeeds`() = runTest {
        val launchPrefs = LaunchPrefs(
            order = Order.DESC,
            launchStatus = LaunchStatus.SUCCESS,
            launchYear = "2024"
        )

        coEvery { spaceXRepository.getLaunchPreferences() } returns launchPrefs

        val result = underTest.invoke()

        assertEquals(launchPrefs, result)
    }
}