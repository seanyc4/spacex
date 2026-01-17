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
    fun `GIVEN database has entries WHEN getTotalEntries THEN returns count`() = runTest {
        coEvery { upcomingDetailDao.getTotalEntries() } returns 10

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(10, result.data)
        coVerify { upcomingDetailDao.getTotalEntries() }
    }

    @Test
    fun `GIVEN database throws exception WHEN getTotalEntries THEN returns error`() = runTest {
        coEvery { upcomingDetailDao.getTotalEntries() } throws RuntimeException("db")

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun `GIVEN launch exists in database WHEN getLaunchDetail THEN returns domain launch`() = runTest {
        val id = "id"
        coEvery { upcomingDetailDao.getById(id) } returns TestData.createLaunchEntity(id = id)

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertEquals(id, result.data!!.id)
    }

    @Test
    fun `GIVEN launch does not exist WHEN getLaunchDetail THEN returns null`() = runTest {
        coEvery { upcomingDetailDao.getById(any()) } returns null

        val result = underTest.getLaunchDetail("missing")

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }

    @Test
    fun `GIVEN launch detail WHEN upsertLaunchDetail THEN succeeds`() = runTest {
        coEvery { upcomingDetailDao.upsert(any()) } returns 1L

        val result = underTest.upsertLaunchDetail(TestData.createLaunch())

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.upsert(any()) }
    }

    @Test
    fun `GIVEN multiple launches WHEN upsertAllLaunchDetails THEN succeeds`() = runTest {
        coEvery { upcomingDetailDao.upsertAll(any()) } returns longArrayOf(1L)

        val result = underTest.upsertAllLaunchDetails(listOf(TestData.createLaunch()))

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.upsertAll(any()) }
    }

    @Test
    fun `GIVEN delete request WHEN deleteAllLaunchDetails THEN succeeds`() = runTest {
        coJustRun { upcomingDetailDao.deleteAll() }

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.deleteAll() }
    }

    @Test
    fun `GIVEN launches list WHEN refreshLaunches THEN delegates to dao`() = runTest {
        coJustRun { upcomingDetailDao.refreshLaunches(any()) }

        underTest.refreshLaunches(listOf(TestData.createLaunch()))

        coVerify { upcomingDetailDao.refreshLaunches(any()) }
    }

    @Test
    fun `GIVEN empty list WHEN upsertAllLaunchDetails THEN succeeds`() = runTest {
        coEvery { upcomingDetailDao.upsertAll(any()) } returns longArrayOf()

        val result = underTest.upsertAllLaunchDetails(emptyList())

        assertTrue(result is LaunchResult.Success)
        coVerify { upcomingDetailDao.upsertAll(emptyList()) }
    }

    @Test
    fun `GIVEN database has no entries WHEN getTotalEntries THEN returns zero`() = runTest {
        coEvery { upcomingDetailDao.getTotalEntries() } returns 0

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data)
    }
}
