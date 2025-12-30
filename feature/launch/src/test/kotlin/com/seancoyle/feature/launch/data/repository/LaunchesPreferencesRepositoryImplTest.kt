package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import io.mockk.impl.annotations.MockK

class LaunchesPreferencesRepositoryImplTest {

    @MockK
    private lateinit var launchesPreferencesDataSource: LaunchesPreferencesDataSource

    private lateinit var underTest: LaunchesPreferencesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesPreferencesRepositoryImpl(launchesPreferencesDataSource)
    }

    @Test
    fun `saveLaunchPreferences saves preferences`() = runTest {
        val order = Order.ASC
        coEvery { launchesPreferencesDataSource.saveLaunchPreferences(order) } just Runs

        underTest.saveLaunchPreferences(order)

        coVerify { launchesPreferencesDataSource.saveLaunchPreferences(order) }
    }

    @Test
    fun `getLaunchPreferences returns preferences`() = runTest {
        val launchPrefs = mockk<LaunchPrefs>()
        coEvery { launchesPreferencesDataSource.getLaunchPreferences() } returns launchPrefs

        val result = underTest.getLaunchPreferences()

        assertEquals(launchPrefs, result)

        coVerify { launchesPreferencesDataSource.getLaunchPreferences() }
    }

}
