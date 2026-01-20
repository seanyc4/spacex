package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.database.dao.UpcomingLaunchDao
import com.seancoyle.database.dao.UpcomingRemoteKeyDao
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
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

class UpcomingLaunchesLocalDataSourceImplTest {

    @MockK
    private lateinit var launchDao: UpcomingLaunchDao

    @MockK
    private lateinit var remoteKeyDao: UpcomingRemoteKeyDao

    @RelaxedMockK
    private lateinit var crashLogger: CrashLogger

    private lateinit var underTest: LaunchesLocalDataSource<UpcomingRemoteKeyEntity>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = UpcomingLaunchesLocalDataSourceImpl(
            launchDao = launchDao,
            remoteKeyDao = remoteKeyDao,
            crashLogger = crashLogger
        )
    }

    @Test
    fun `GIVEN remote keys exist WHEN getRemoteKeys THEN returns list of remote keys`() = runTest {
        val remoteKey1 = UpcomingRemoteKeyEntity(
            id = "1",
            nextKey = 1,
            prevKey = null,
            currentPage = 0,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        val remoteKey2 = UpcomingRemoteKeyEntity(
            id = "2",
            nextKey = 2,
            prevKey = 0,
            currentPage = 1,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { remoteKeyDao.getAll() } returns listOf(remoteKey1, remoteKey2)

        val result = underTest.getRemoteKeys()

        assertEquals(2, result.size)
        assertEquals("1", result[0]?.id)
        assertEquals("2", result[1]?.id)
        coVerify { remoteKeyDao.getAll() }
    }

    @Test
    fun `GIVEN exception thrown WHEN getRemoteKeys THEN returns empty list`() = runTest {
        coEvery { remoteKeyDao.getAll() } throws RuntimeException("Database error")

        val result = underTest.getRemoteKeys()

        assertEquals(0, result.size)
        coVerify { remoteKeyDao.getAll() }
    }

    @Test
    fun `GIVEN remote key exists WHEN getRemoteKey THEN returns remote key for given id`() = runTest {
        val id = "test-id"
        val remoteKey = UpcomingRemoteKeyEntity(
            id = id,
            nextKey = 1,
            prevKey = null,
            currentPage = 0,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { remoteKeyDao.getById(id) } returns remoteKey

        val result = underTest.getRemoteKey(id)

        assertNotNull(result)
        assertEquals(id, result.id)
        assertEquals(1, result.nextKey)
        coVerify { remoteKeyDao.getById(id) }
    }

    @Test
    fun `GIVEN exception thrown WHEN getRemoteKey THEN returns null`() = runTest {
        val id = "test-id"
        coEvery { remoteKeyDao.getById(id) } throws RuntimeException("Database error")

        val result = underTest.getRemoteKey(id)

        assertNull(result)
        coVerify { remoteKeyDao.getById(id) }
    }

    @Test
    fun `GIVEN launches and keys WHEN refreshWithKeys THEN clears and inserts launches with remote keys`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())
        coJustRun { launchDao.refreshLaunchesWithKeys(any(), any(), any(), any(), any(), any()) }

        underTest.refreshWithKeys(
            launches = launches,
            nextPage = 1,
            prevPage = null,
            currentPage = 0,
            cachedQuery = "",
            cachedLaunchStatus = null
        )

        coVerify { launchDao.refreshLaunchesWithKeys(any(), remoteKeyDao, any(), 1, null, 0) }
    }

    @Test
    fun `GIVEN launches and keys WHEN appendWithKeys THEN upserts launches and remote keys`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())
        coEvery { launchDao.upsertAll(any()) } returns longArrayOf(1L)
        coJustRun { remoteKeyDao.upsertAll(any()) }

        underTest.appendWithKeys(
            launches = launches,
            nextPage = 2,
            prevPage = 0,
            currentPage = 1,
            cachedQuery = "",
            cachedLaunchStatus = null
        )

        coVerify { launchDao.upsertAll(any()) }
        coVerify { remoteKeyDao.upsertAll(any()) }
    }
}
