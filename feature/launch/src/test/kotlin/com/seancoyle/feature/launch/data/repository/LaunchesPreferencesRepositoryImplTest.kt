package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LaunchesPreferencesRepositoryImplTest {

    @MockK
    private lateinit var dataSource: LaunchesPreferencesDataSource

    private lateinit var underTest: LaunchesPreferencesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesPreferencesRepositoryImpl(dataSource)
    }

    @Test
    fun `GIVEN preferences exist WHEN getLaunchPreferences THEN returns preferences from data source`() = runTest {
        val expectedPrefs = LaunchPrefs(order = Order.DESC)
        coEvery { dataSource.getLaunchPreferences() } returns expectedPrefs

        val result = underTest.getLaunchPreferences()

        assertEquals(expectedPrefs, result)
        coVerify { dataSource.getLaunchPreferences() }
    }

    @Test
    fun `GIVEN order WHEN saveLaunchPreferences THEN delegates to data source`() = runTest {
        val order = Order.ASC
        coJustRun { dataSource.saveLaunchPreferences(order) }

        underTest.saveLaunchPreferences(order)

        coVerify { dataSource.saveLaunchPreferences(order) }
    }

    @Test
    fun `GIVEN DESC order WHEN saveLaunchPreferences THEN saves DESC order`() = runTest {
        val order = Order.DESC
        coJustRun { dataSource.saveLaunchPreferences(order) }

        underTest.saveLaunchPreferences(order)

        coVerify { dataSource.saveLaunchPreferences(Order.DESC) }
    }
}
