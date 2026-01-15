package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.Crashlytics
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
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: DetailLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = PastDetailLocalDataSourceImpl(
            pastDetailDao = pastDetailDao,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getTotalEntries returns count`() = runTest {
        coEvery { pastDetailDao.getTotalEntries() } returns 10

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(10, result.data)
        coVerify { pastDetailDao.getTotalEntries() }
    }

    @Test
    fun `getTotalEntries returns error on exception`() = runTest {
        coEvery { pastDetailDao.getTotalEntries() } throws RuntimeException("db")

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun `getLaunchDetail returns domain launch when found`() = runTest {
        val id = "id"
        coEvery { pastDetailDao.getById(id) } returns TestData.createPastDetailEntity(id = id)

        val result = underTest.getLaunchDetail(id)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertEquals(id, result.data!!.id)
    }

    @Test
    fun `getLaunchDetail returns null when missing`() = runTest {
        coEvery { pastDetailDao.getById(any()) } returns null

        val result = underTest.getLaunchDetail("missing")

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
    }

    @Test
    fun `upsertLaunchDetail succeeds`() = runTest {
        coEvery { pastDetailDao.upsert(any()) } returns 1L

        val result = underTest.upsertLaunchDetail(TestData.createLaunch())

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.upsert(any()) }
    }

    @Test
    fun `upsertAllLaunchDetails succeeds`() = runTest {
        coEvery { pastDetailDao.upsertAll(any()) } returns longArrayOf(1L)

        val result = underTest.upsertAllLaunchDetails(listOf(TestData.createLaunch()))

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.upsertAll(any()) }
    }

    @Test
    fun `deleteAllLaunchDetails succeeds`() = runTest {
        coJustRun { pastDetailDao.deleteAll() }

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Success)
        coVerify { pastDetailDao.deleteAll() }
    }

    @Test
    fun `refreshLaunches delegates`() = runTest {
        coJustRun { pastDetailDao.refreshLaunches(any()) }

        underTest.refreshLaunches(listOf(TestData.createLaunch()))

        coVerify { pastDetailDao.refreshLaunches(any()) }
    }
}
