package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.LaunchDetailDao
import com.seancoyle.database.dao.LaunchRemoteKeyDao
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
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

private const val ID = "test-id"

class LaunchLocalDataSourceImplTest {

    @MockK
    private lateinit var launchDao: LaunchDao

    @MockK
    private lateinit var launchDetailDao: LaunchDetailDao

    @MockK
    private lateinit var remoteKeyDao: LaunchRemoteKeyDao

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchesLocalDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesLocalDataSourceImpl(
            launchDao = launchDao,
            launchDetailDao = launchDetailDao,
            remoteKeyDao = remoteKeyDao,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getRemoteKeys returns list of remote keys`() = runTest {
        val remoteKey1 = LaunchRemoteKeyEntity(
            id = "1",
            nextKey = 1,
            prevKey = null,
            currentPage = 0,
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name,
            cachedLaunchStatus = null
        )
        val remoteKey2 = LaunchRemoteKeyEntity(
            id = "2",
            nextKey = 2,
            prevKey = 0,
            currentPage = 1,
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name,
            cachedLaunchStatus = null
        )
        coEvery { remoteKeyDao.getRemoteKeys() } returns listOf(remoteKey1, remoteKey2)

        val result = underTest.getRemoteKeys()

        assertEquals(2, result.size)
        assertEquals("1", result[0]?.id)
        assertEquals("2", result[1]?.id)
        coVerify { remoteKeyDao.getRemoteKeys() }
    }

    @Test
    fun `getRemoteKeys returns empty list on exception`() = runTest {
        coEvery { remoteKeyDao.getRemoteKeys() } throws RuntimeException("Database error")

        val result = underTest.getRemoteKeys()

        assertEquals(0, result.size)
        coVerify { remoteKeyDao.getRemoteKeys() }
    }

    @Test
    fun `getRemoteKey returns remote key for given id`() = runTest {
        val remoteKey = LaunchRemoteKeyEntity(
            id = ID,
            nextKey = 1,
            prevKey = null,
            currentPage = 0,
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name,
            cachedLaunchStatus = null
        )
        coEvery { remoteKeyDao.getRemoteKey(ID) } returns remoteKey

        val result = underTest.getRemoteKey(ID)

        assertNotNull(result)
        assertEquals(ID, result.id)
        assertEquals(1, result.nextKey)
        coVerify { remoteKeyDao.getRemoteKey(ID) }
    }

    @Test
    fun `getRemoteKey returns null on exception`() = runTest {
        coEvery { remoteKeyDao.getRemoteKey(ID) } throws RuntimeException("Database error")

        val result = underTest.getRemoteKey(ID)

        assertNull(result)
        coVerify { remoteKeyDao.getRemoteKey(ID) }
    }

    @Test
    fun `refreshLaunchesWithKeys clears and inserts launches with remote keys`() = runTest {
        val launches = listOf(TestData.createLaunchSummary(), TestData.createLaunchSummary(id = "2"))
        val nextPage = 1
        val prevPage = null
        val currentPage = 0

        coJustRun {
            launchDao.refreshLaunchesWithKeys(
                launches = any(),
                remoteKeyDao = remoteKeyDao,
                nextPage = nextPage,
                prevPage = prevPage,
                currentPage = currentPage,
                remoteKeys = any()
            )
        }

        underTest.refreshLaunchesWithKeys(launches, nextPage, prevPage, currentPage)

        coVerify {
            launchDao.refreshLaunchesWithKeys(
                launches = any(),
                remoteKeyDao = remoteKeyDao,
                nextPage = nextPage,
                prevPage = prevPage,
                currentPage = currentPage,
                remoteKeys = any()
            )
        }
    }

    @Test
    fun `appendLaunchesWithKeys upserts launches and remote keys`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())
        val nextPage = 2
        val prevPage = 0
        val currentPage = 1

        coJustRun { launchDao.upsertAll(any()) }
        coJustRun { remoteKeyDao.upsertAll(any()) }

        underTest.appendLaunchesWithKeys(launches, nextPage, prevPage, currentPage)

        coVerify { launchDao.upsertAll(any()) }
        coVerify { remoteKeyDao.upsertAll(any()) }
    }

    @Test
    fun `refreshLaunches clears and inserts launches without remote keys`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())

        coJustRun { launchDao.refreshLaunches(any()) }

        underTest.refreshLaunches(launches)

        coVerify { launchDao.refreshLaunches(any()) }
    }

    @Test
    fun `upsert inserts launch and returns success`() = runTest {
        val launch = TestData.createLaunchSummary()
        coEvery { launchDao.upsert(any()) } returns 1L

        val result = underTest.upsert(launch)

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDao.upsert(any()) }
    }

    @Test
    fun `upsert returns error on exception`() = runTest {
        val launch = TestData.createLaunchSummary()
        val exception = RuntimeException("Database error")
        coEvery { launchDao.upsert(any()) } throws exception

        val result = underTest.upsert(launch)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
        coVerify { launchDao.upsert(any()) }
    }

    @Test
    fun `upsertAll inserts multiple launches and returns success`() = runTest {
        val launches = listOf(TestData.createLaunchSummary(), TestData.createLaunchSummary(id = "2"))
        coEvery { launchDao.upsertAll(any()) } returns longArrayOf(1L, 2L)

        val result = underTest.upsertAll(launches)

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDao.upsertAll(any()) }
    }

    @Test
    fun `upsertAll returns error on exception`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())
        val exception = RuntimeException("Database error")
        coEvery { launchDao.upsertAll(any()) } throws exception

        val result = underTest.upsertAll(launches)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
        coVerify { launchDao.upsertAll(any()) }
    }

    @Test
    fun `deleteAll deletes all launches and returns success`() = runTest {
        coJustRun { launchDao.deleteAll() }

        val result = underTest.deleteAll()

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDao.deleteAll() }
    }

    @Test
    fun `deleteAll returns error on exception`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { launchDao.deleteAll() } throws exception

        val result = underTest.deleteAll()

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
        coVerify { launchDao.deleteAll() }
    }

    @Test
    fun `getById returns launch when entity exists`() = runTest {
        val launchId = "faf4a0bc-7dad-4842-b74c-73a9f648b5cc"
        val launchEntity = TestData.createLaunchSummaryEntity()
        coEvery { launchDao.getById(launchId) } returns launchEntity

        val result = underTest.getById(launchId)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launchId, result.data?.id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", result.data?.missionName)
        coVerify { launchDao.getById(launchId) }
    }

    @Test
    fun `getById returns error on exception`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { launchDao.getById(ID) } throws exception

        val result = underTest.getById(ID)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
        coVerify { launchDao.getById(ID) }
    }

    @Test
    fun `getTotalEntries returns count of launches`() = runTest {
        coEvery { launchDao.getTotalEntries() } returns 10

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Success)
        assertEquals(10, result.data)
        coVerify { launchDao.getTotalEntries() }
    }

    @Test
    fun `getTotalEntries returns error on exception`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { launchDao.getTotalEntries() } throws exception

        val result = underTest.getTotalEntries()

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
        coVerify { launchDao.getTotalEntries() }
    }

    @Test
    fun `getTotalEntriesByType returns count of launches for specific type`() = runTest {
        val launchType = LaunchesType.UPCOMING.name
        coEvery { launchDao.getTotalEntriesByType(launchType) } returns 5

        val result = underTest.getTotalEntriesByType(launchType)

        assertTrue(result is LaunchResult.Success)
        assertEquals(5, result.data)
        coVerify { launchDao.getTotalEntriesByType(launchType) }
    }

    @Test
    fun `getLaunchDetail returns launch when found in database`() = runTest {
        val launchEntity = TestData.createLaunchEntity()
        coEvery { launchDetailDao.getById(ID) } returns launchEntity

        val result = underTest.getLaunchDetail(ID)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertEquals(launchEntity.id, result.data?.id)
        coVerify { launchDetailDao.getById(ID) }
    }

    @Test
    fun `getLaunchDetail returns null when not found in database`() = runTest {
        coEvery { launchDetailDao.getById(ID) } returns null

        val result = underTest.getLaunchDetail(ID)

        assertTrue(result is LaunchResult.Success)
        assertNull(result.data)
        coVerify { launchDetailDao.getById(ID) }
    }

    @Test
    fun `getLaunchDetail returns error on exception`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { launchDetailDao.getById(ID) } throws exception

        val result = underTest.getLaunchDetail(ID)

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
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
    fun `upsertAllLaunchDetails inserts all launches successfully`() = runTest {
        val launches = listOf(TestData.createLaunch(), TestData.createLaunch(id = "id-2"))
        coEvery { launchDetailDao.upsertAll(any()) } returns longArrayOf(1L, 2L)

        val result = underTest.upsertAllLaunchDetails(launches)

        assertTrue(result is LaunchResult.Success)
        coVerify { launchDetailDao.upsertAll(any()) }
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
        val exception = RuntimeException("Database error")
        coEvery { launchDetailDao.deleteAll() } throws exception

        val result = underTest.deleteAllLaunchDetails()

        assertTrue(result is LaunchResult.Error)
        assertEquals(exception, result.error)
    }
}
