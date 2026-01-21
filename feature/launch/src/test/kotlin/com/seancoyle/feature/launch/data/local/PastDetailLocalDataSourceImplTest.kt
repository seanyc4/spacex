package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.PastDetailDao
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

class PastDetailLocalDataSourceImplTest {

    @MockK
    private lateinit var pastDetailDao: PastDetailDao

    @RelaxedMockK
    private lateinit var crashLogger: CrashLogger

    private lateinit var underTest: DetailLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = PastDetailLocalDataSourceImpl(
            pastDetailDao = pastDetailDao,
            crashLogger = crashLogger
        )
    }

    @Test
    fun `GIVEN database has entries WHEN getTotalEntries THEN returns count`() = runTest {
        coEvery { pastDetailDao.getTotalEntries() } returns 10

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(10, result.data)
        coVerify { pastDetailDao.getTotalEntries() }
    }

    @Test
    fun `GIVEN database throws exception WHEN getTotalEntries THEN returns error`() = runTest {
        coEvery { pastDetailDao.getTotalEntries() } throws RuntimeException("db")

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun `GIVEN launch exists in database WHEN getLaunchDetail THEN returns domain launch`() = runTest {
        val id = "id"
        coEvery { pastDetailDao.getById(id) } returns TestData.createPastDetailEntity(id = id)

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertEquals(id, result.data!!.id)
    }

    @Test
    fun `GIVEN launch does not exist WHEN getLaunchDetail THEN returns null`() = runTest {
        coEvery { pastDetailDao.getById(any()) } returns null

        val result = underTest.getLaunchDetail("missing")

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }

    @Test
    fun `GIVEN launch detail WHEN upsertLaunchDetail THEN succeeds`() = runTest {
        coEvery { pastDetailDao.upsert(any()) } returns 1L

        val result = underTest.upsertLaunchDetail(TestData.createLaunch())

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.upsert(any()) }
    }

    @Test
    fun `GIVEN multiple launches WHEN upsertAllLaunchDetails THEN succeeds`() = runTest {
        coEvery { pastDetailDao.upsertAll(any()) } returns longArrayOf(1L)

        val result = underTest.upsertAllLaunchDetails(listOf(TestData.createLaunch()))

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.upsertAll(any()) }
    }

    @Test
    fun `GIVEN delete request WHEN deleteAllLaunchDetails THEN succeeds`() = runTest {
        coJustRun { pastDetailDao.deleteAll() }

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.deleteAll() }
    }

    @Test
    fun `GIVEN launches list WHEN refreshLaunches THEN delegates to dao`() = runTest {
        coJustRun { pastDetailDao.refreshLaunches(any()) }

        underTest.refreshLaunches(listOf(TestData.createLaunch()))

        coVerify { pastDetailDao.refreshLaunches(any()) }
    }

    @Test
    fun `GIVEN empty list WHEN upsertAllLaunchDetails THEN succeeds`() = runTest {
        coEvery { pastDetailDao.upsertAll(any()) } returns longArrayOf()

        val result = underTest.upsertAllLaunchDetails(emptyList())

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.upsertAll(emptyList()) }
    }

    @Test
    fun `GIVEN database has no entries WHEN getTotalEntries THEN returns zero`() = runTest {
        coEvery { pastDetailDao.getTotalEntries() } returns 0

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data)
    }
}
