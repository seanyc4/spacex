package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.repository.LaunchPreferencesRepository
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
        coEvery { launchPreferencesDataSource.saveLaunchPreferences(order) } just Runs

        underTest.saveLaunchPreferences(order)

        coVerify { launchPreferencesDataSource.saveLaunchPreferences(order) }
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
