package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.LaunchDetailDao
import com.seancoyle.feature.launch.data.repository.LaunchDetailLocalDataSource
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LaunchDetailLocalDataSourceImplTest {

    @MockK
    private lateinit var launchDetailDao: LaunchDetailDao

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchDetailLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchDetailLocalDataSourceImpl(
            launchDetailDao = launchDetailDao,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getTotalEntries returns count of launches`() = runTest {
        val expectedCount = 10
        coEvery { launchDetailDao.getTotalEntries() } returns expectedCount

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(expectedCount, result.data)
        coVerify { launchDetailDao.getTotalEntries() }
    }

    @Test
    fun `getTotalEntries returns error on exception`() = runTest {
        coEvery { launchDetailDao.getTotalEntries() } throws RuntimeException("Database error")

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Error)
        coVerify { launchDetailDao.getTotalEntries() }
    }

    @Test
    fun `getLaunchDetail returns launch when found in database`() = runTest {
        val id = "test-id"
        val launchEntity = TestData.createLaunchEntity()
        coEvery { launchDetailDao.getById(id) } returns launchEntity

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        coVerify { launchDetailDao.getById(id) }
    }

    @Test
    fun `getLaunchDetail returns null when not found in database`() = runTest {
        val id = "non-existent-id"
        coEvery { launchDetailDao.getById(id) } returns null

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
        coVerify { launchDetailDao.getById(id) }
    }

    @Test
    fun `getLaunchDetail returns error on exception`() = runTest {
        val id = "test-id"
        coEvery { launchDetailDao.getById(id) } throws RuntimeException("Database error")

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Error)
        coVerify { launchDetailDao.getById(id) }
    }

    @Test
    fun `upsertLaunchDetail inserts launch successfully`() = runTest {
        val launch = TestData.createLaunch()
        coEvery { launchDetailDao.upsert(any()) } returns 1L

        val result = underTest.upsertLaunchDetail(launch)

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDetailDao.upsert(any()) }
    }

    @Test
    fun `upsertLaunchDetail returns error on exception`() = runTest {
        val launch = TestData.createLaunch()
        coEvery { launchDetailDao.upsert(any()) } throws RuntimeException("Database error")

        val result = underTest.upsertLaunchDetail(launch)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun `upsertAllLaunchDetails inserts all launches successfully`() = runTest {
        val launches = listOf(TestData.createLaunch(), TestData.createLaunch())
        coEvery { launchDetailDao.upsertAll(any()) } returns longArrayOf(1L, 2L)

        val result = underTest.upsertAllLaunchDetails(launches)

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDetailDao.upsertAll(any()) }
    }

    @Test
    fun `upsertAllLaunchDetails returns error on exception`() = runTest {
        val launches = listOf(TestData.createLaunch())
        coEvery { launchDetailDao.upsertAll(any()) } throws RuntimeException("Database error")

        val result = underTest.upsertAllLaunchDetails(launches)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun `deleteAllLaunchDetails deletes all launches successfully`() = runTest {
        coJustRun { launchDetailDao.deleteAll() }

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDetailDao.deleteAll() }
    }

    @Test
    fun `deleteAllLaunchDetails returns error on exception`() = runTest {
        coEvery { launchDetailDao.deleteAll() } throws RuntimeException("Database error")

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Error)
    }
}

