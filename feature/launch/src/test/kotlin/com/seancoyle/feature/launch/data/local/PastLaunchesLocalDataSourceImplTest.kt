package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.dao.PastRemoteKeyDao
import com.seancoyle.database.entities.PastRemoteKeyEntity
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

class PastLaunchesLocalDataSourceImplTest {

    @MockK
    private lateinit var launchDao: PastLaunchDao

    @MockK
    private lateinit var remoteKeyDao: PastRemoteKeyDao

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: LaunchesLocalDataSource<PastRemoteKeyEntity>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = PastLaunchesLocalDataSourceImpl(
            launchDao = launchDao,
            remoteKeyDao = remoteKeyDao,
            crashlytics = crashlytics
        )
    }

    @Test
    fun `getRemoteKeys returns list of remote keys`() = runTest {
        val remoteKey1 = PastRemoteKeyEntity(
            id = "1",
            nextKey = 1,
            prevKey = null,
            currentPage = 0,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        val remoteKey2 = PastRemoteKeyEntity(
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
    fun `getRemoteKeys returns empty list on exception`() = runTest {
        coEvery { remoteKeyDao.getAll() } throws RuntimeException("Database error")

        val result = underTest.getRemoteKeys()

        assertEquals(0, result.size)
        coVerify { remoteKeyDao.getAll() }
    }

    @Test
    fun `getRemoteKey returns remote key for given id`() = runTest {
        val id = "test-id"
        val remoteKey = PastRemoteKeyEntity(
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
    fun `getRemoteKey returns null on exception`() = runTest {
        val id = "test-id"
        coEvery { remoteKeyDao.getById(id) } throws RuntimeException("Database error")

        val result = underTest.getRemoteKey(id)

        assertNull(result)
        coVerify { remoteKeyDao.getById(id) }
    }

    @Test
    fun `refreshWithKeys clears and inserts launches with remote keys`() = runTest {
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
    fun `appendWithKeys upserts launches and remote keys`() = runTest {
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
