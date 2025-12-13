package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.*
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import io.mockk.impl.annotations.MockK

class LaunchPreferencesRepositoryImplTest {

    @MockK
    private lateinit var launchPreferencesDataSource: LaunchPreferencesDataSource

    private lateinit var underTest: LaunchPreferencesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchPreferencesRepositoryImpl(launchPreferencesDataSource)
    }

    @Test
    fun `saveLaunchPreferences saves preferences`() = runTest {
        val order = Order.ASC
        val launchStatus = LaunchStatus.SUCCESS
        val launchYear = "2022"
        coEvery { launchPreferencesDataSource.saveLaunchPreferences(order, launchStatus, launchYear) } just Runs

        underTest.saveLaunchPreferences(order, launchStatus, launchYear)

        coVerify { launchPreferencesDataSource.saveLaunchPreferences(order, launchStatus, launchYear) }
    }

    @Test
    fun `getLaunchPreferences returns preferences`() = runTest {
        val launchPrefs = mockk<LaunchPrefs>()
        coEvery { launchPreferencesDataSource.getLaunchPreferences() } returns launchPrefs

        val result = underTest.getLaunchPreferences()

        assertEquals(launchPrefs, result)

        coVerify { launchPreferencesDataSource.getLaunchPreferences() }
    }

}