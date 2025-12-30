package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.presentation.launches.LaunchesConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.presentation.launches.LaunchesConstants.PREFETCH_DISTANCE
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalPagingApi::class)
class LaunchesRemoteMediatorTest {

    @MockK
    private lateinit var launchesRemoteDataSource: LaunchesRemoteDataSource

    @MockK
    private lateinit var launchesLocalDataSource: LaunchesLocalDataSource

    private lateinit var launchesQuery: LaunchesQuery
    private lateinit var underTest: LaunchRemoteMediator

    private val pagingConfig = PagingConfig(
        pageSize = PAGINATION_LIMIT,
        enablePlaceholders = true,
        initialLoadSize = PAGINATION_LIMIT * 2,
        prefetchDistance = PREFETCH_DISTANCE
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        launchesQuery = LaunchesQuery(query = "")
        underTest = LaunchRemoteMediator(
            launchesRemoteDataSource = launchesRemoteDataSource,
            launchesLocalDataSource = launchesLocalDataSource,
            launchesQuery = launchesQuery
        )
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when search query has changed from cached`() = runTest {
        launchesQuery = LaunchesQuery(query = "Falcon", launchesType = LaunchesType.UPCOMING)
        underTest = LaunchRemoteMediator(
            launchesRemoteDataSource = launchesRemoteDataSource,
            launchesLocalDataSource = launchesLocalDataSource,
            launchesQuery = launchesQuery
        )

        val remoteKey = LaunchRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `initialize returns SKIP_INITIAL_REFRESH when cache is valid and query hasn't changed`() = runTest {
        val currentTime = System.currentTimeMillis()
        val recentTimestamp = currentTime - TimeUnit.MINUTES.toMillis(30) // 30 minutes ago
        launchesQuery = LaunchesQuery(query = "Falcon", launchesType = LaunchesType.UPCOMING)
        underTest = LaunchRemoteMediator(
            launchesRemoteDataSource = launchesRemoteDataSource,
            launchesLocalDataSource = launchesLocalDataSource,
            launchesQuery = launchesQuery
        )
        val remoteKey = LaunchRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = recentTimestamp,
            cachedQuery = "Falcon",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, result)
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when cache is stale but query hasn't changed`() = runTest {
        val currentTime = System.currentTimeMillis()
        val staleTimestamp = currentTime - TimeUnit.HOURS.toMillis(2) // 2 hours ago
        launchesQuery = LaunchesQuery(query = "Falcon", launchesType = LaunchesType.UPCOMING)
        underTest = LaunchRemoteMediator(
            launchesRemoteDataSource = launchesRemoteDataSource,
            launchesLocalDataSource = launchesLocalDataSource,
            launchesQuery = launchesQuery
        )
        val remoteKey = LaunchRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = staleTimestamp,
            cachedQuery = "Falcon",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when no remote keys exist`() = runTest {
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `load refresh succeeds and clears old data`() = runTest {
        val launches = List(PAGINATION_LIMIT) { TestData.createLaunch(id = "id-$it") }
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getLaunches(0, launchesQuery) } returns LaunchResult.Success(launches)
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(0)
        coJustRun {
            launchesLocalDataSource.refreshLaunchesWithKeys(
                launches = launches,
                nextPage = 1,
                prevPage = null,
                currentPage = 0,
                cachedQuery = any(),
                cachedLaunchType = any()
            )
        }

        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, result.endOfPaginationReached)
        coVerify {
            launchesLocalDataSource.refreshLaunchesWithKeys(
                launches = launches,
                nextPage = 1,
                prevPage = null,
                currentPage = 0,
                cachedQuery = "",
                cachedLaunchType = LaunchesType.UPCOMING.name
            )
        }
    }

    @Test
    fun `load refresh succeeds with end of pagination reached`() = runTest {
        val launches = List(5) { TestData.createLaunch(id = "id-$it") }
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(0)
        coEvery { launchesRemoteDataSource.getLaunches(0, launchesQuery) } returns LaunchResult.Success(launches)
        coJustRun {
            launchesLocalDataSource.refreshLaunchesWithKeys(
                launches = launches,
                nextPage = null,
                prevPage = null,
                currentPage = 0,
                cachedQuery = any(),
                cachedLaunchType = any()
            )
        }

        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(true, result.endOfPaginationReached)
    }

    @Test
    fun `load refresh fails with no cached data returns error`() = runTest {
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getLaunches(0, launchesQuery) } returns LaunchResult.Error(RuntimeException("Network error"))
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(0)

        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load refresh fails with cached data available shows cache`() = runTest {
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getLaunches(0, launchesQuery) } returns LaunchResult.Error(RuntimeException("Network error"))
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(10)

        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, result.endOfPaginationReached)
    }

    @Test
    fun `load refresh with exception and cached data shows cache`() = runTest {
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getLaunches(0, launchesQuery) } throws RuntimeException("Unexpected error")
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(10)

        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, result.endOfPaginationReached)
    }

    @Test
    fun `load append succeeds with more data`() = runTest {
        val launches = List(PAGINATION_LIMIT) { TestData.createLaunch(id = "id-$it") }
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "last-id",
            prevKey = 0,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } returns LaunchResult.Success(launches)
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(0)
        coJustRun {
            launchesLocalDataSource.appendLaunchesWithKeys(
                launches = launches,
                nextPage = 2,
                prevPage = 0,
                currentPage = 1,
                cachedQuery = any(),
                cachedLaunchType = any()
            )
        }

        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, result.endOfPaginationReached)
    }

    @Test
    fun `load append succeeds with end of pagination reached`() = runTest {
        val launches = List(PAGINATION_LIMIT - 1) { TestData.createLaunch(id = "id-$it") }
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "last-id",
            prevKey = 0,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } returns LaunchResult.Success(launches)
        coJustRun {
            launchesLocalDataSource.appendLaunchesWithKeys(
                launches = launches,
                nextPage = null,
                prevPage = 0,
                currentPage = 1,
                cachedQuery = any(),
                cachedLaunchType = any()
            )
        }

        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(true, result.endOfPaginationReached)
    }

    @Test
    fun `load append when nextKey is null returns end of pagination`() = runTest {
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "last-id",
            prevKey = 0,
            nextKey = null,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(true, result.endOfPaginationReached)
    }

    @Test
    fun `load append when remoteKey is null returns not end of pagination`() = runTest {
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()

        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, result.endOfPaginationReached)
    }

    @Test
    fun `when load append fails returns error`() = runTest {
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "last-id",
            prevKey = 0,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } returns LaunchResult.Error(Throwable("Network error"))
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(10)

        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load append with exception returns error`() = runTest {
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "last-id",
            prevKey = 0,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } throws RuntimeException("Unexpected error")
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(10)

        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load prepend succeeds with more data`() = runTest {
        val launches = List(PAGINATION_LIMIT) { TestData.createLaunch(id = "id-$it") }
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "first-id",
            prevKey = 1,
            nextKey = 2,
            currentPage = 1,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } returns LaunchResult.Success(launches)
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(0)
        coJustRun {
            launchesLocalDataSource.appendLaunchesWithKeys(
                launches = launches,
                nextPage = 2,
                prevPage = 0,
                currentPage = 1,
                cachedQuery = any(),
                cachedLaunchType = any()
            )
        }

        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, result.endOfPaginationReached)
    }

    @Test
    fun `load prepend when prevKey is null returns end of pagination`() = runTest {
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "first-id",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(true, result.endOfPaginationReached)
    }

    @Test
    fun `load prepend when remoteKey is null returns end of pagination`() = runTest {
        val pagingState = createPagingState()
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns emptyList()

        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(true, result.endOfPaginationReached)
    }

    @Test
    fun `load prepend fails returns error for retry button`() = runTest {
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "first-id",
            prevKey = 1,
            nextKey = 2,
            currentPage = 1,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } returns LaunchResult.Error(RuntimeException("Network error"))
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(10)

        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load prepend with exception returns error for retry button`() = runTest {
        val pagingState = createPagingState()
        val remoteKey = LaunchRemoteKeyEntity(
            id = "first-id",
            prevKey = 1,
            nextKey = 2,
            currentPage = 1,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchType = LaunchesType.UPCOMING.name
        )
        coEvery { launchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getLaunches(1, launchesQuery) } throws RuntimeException("Unexpected error")
        coEvery { launchesLocalDataSource.getTotalEntries() } returns LaunchResult.Success(10)

        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    private fun createPagingState(): PagingState<Int, LaunchEntity> {
        return PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )
    }
}
