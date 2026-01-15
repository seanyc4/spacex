package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.UpcomingDetailDao
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
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

class UpcomingDetailLocalDataSourceImplTest {

    @MockK
    private lateinit var upcomingDetailDao: UpcomingDetailDao

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: DetailLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = UpcomingDetailLocalDataSourceImpl(
            upcomingDetailDao = upcomingDetailDao,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getTotalEntries returns count`() = runTest {
        coEvery { upcomingDetailDao.getTotalEntries() } returns 10

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(10, result.data)
        coVerify { upcomingDetailDao.getTotalEntries() }
    }

    @Test
    fun `getTotalEntries returns error on exception`() = runTest {
        coEvery { upcomingDetailDao.getTotalEntries() } throws RuntimeException("db")

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun `getLaunchDetail returns domain launch when found`() = runTest {
        val id = "id"
        coEvery { upcomingDetailDao.getById(id) } returns TestData.createLaunchEntity(id = id)

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertEquals(id, result.data!!.id)
    }

    @Test
    fun `getLaunchDetail returns null when missing`() = runTest {
        coEvery { upcomingDetailDao.getById(any()) } returns null

        val result = underTest.getLaunchDetail("missing")

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }

    @Test
    fun `upsertLaunchDetail succeeds`() = runTest {
        coEvery { upcomingDetailDao.upsert(any()) } returns 1L

        val result = underTest.upsertLaunchDetail(TestData.createLaunch())

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.upsert(any()) }
    }

    @Test
    fun `upsertAllLaunchDetails succeeds`() = runTest {
        coEvery { upcomingDetailDao.upsertAll(any()) } returns longArrayOf(1L)

        val result = underTest.upsertAllLaunchDetails(listOf(TestData.createLaunch()))

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.upsertAll(any()) }
    }

    @Test
    fun `deleteAllLaunchDetails succeeds`() = runTest {
        coJustRun { upcomingDetailDao.deleteAll() }

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.deleteAll() }
    }

    @Test
    fun `refreshLaunches delegates`() = runTest {
        coJustRun { upcomingDetailDao.refreshLaunches(any()) }

        underTest.refreshLaunches(listOf(TestData.createLaunch()))

        coVerify { upcomingDetailDao.refreshLaunches(any()) }
    }
}
