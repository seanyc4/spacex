package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.LaunchDetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.data.repository.UpcomingLaunchesLocalDataSource
import com.seancoyle.feature.launch.domain.model.DetailedLaunchesResult
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PREFETCH_DISTANCE
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalPagingApi::class)
class UpcomingLaunchesRemoteMediatorTest {

    @MockK
    private lateinit var launchesRemoteDataSource: LaunchesRemoteDataSource

    @MockK
    private lateinit var upcomingLaunchesLocalDataSource: UpcomingLaunchesLocalDataSource

    @MockK
    private lateinit var launchDetailLocalDataSource: LaunchDetailLocalDataSource

    private lateinit var launchesQuery: LaunchesQuery
    private lateinit var underTest: UpcomingLaunchesRemoteMediator

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
        underTest = UpcomingLaunchesRemoteMediator(
            remotedataSource = launchesRemoteDataSource,
            localDataSource = upcomingLaunchesLocalDataSource,
            launchDetailLocalDataSource = launchDetailLocalDataSource,
            launchesQuery = launchesQuery
        )
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when no remote keys exist`() = runTest {
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when cache is stale but query hasn't changed`() = runTest {
        val staleCreatedAt = System.currentTimeMillis() - (2 * 60 * 60 * 1000) // 2 hours ago
        val remoteKey = UpcomingRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = staleCreatedAt,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `initialize returns SKIP_INITIAL_REFRESH when cache is valid and query hasn't changed`() = runTest {
        val recentCreatedAt = System.currentTimeMillis() // Just now
        val remoteKey = UpcomingRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = recentCreatedAt,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, result)
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when search query has changed from cached`() = runTest {
        launchesQuery = LaunchesQuery(query = "Falcon")
        underTest = UpcomingLaunchesRemoteMediator(
            remotedataSource = launchesRemoteDataSource,
            localDataSource = upcomingLaunchesLocalDataSource,
            launchDetailLocalDataSource = launchDetailLocalDataSource,
            launchesQuery = launchesQuery
        )

        val remoteKey = UpcomingRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `load refresh succeeds and clears old data`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())
        val detailedResult = DetailedLaunchesResult(
            summaries = launches,
            details = listOf(TestData.createLaunch())
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getUpcomingDetailedLaunches(any(), any()) } returns LaunchResult.Success(detailedResult)
        coJustRun { upcomingLaunchesLocalDataSource.refreshWithKeys(any(), any(), any(), any(), any(), any()) }
        coJustRun { launchDetailLocalDataSource.refreshLaunches(any()) }

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify { upcomingLaunchesLocalDataSource.refreshWithKeys(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `load refresh succeeds with end of pagination reached`() = runTest {
        val detailedResult = DetailedLaunchesResult(
            summaries = emptyList(),
            details = emptyList()
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getUpcomingDetailedLaunches(any(), any()) } returns LaunchResult.Success(detailedResult)
        coJustRun { upcomingLaunchesLocalDataSource.refreshWithKeys(any(), any(), any(), any(), any(), any()) }
        coJustRun { launchDetailLocalDataSource.refreshLaunches(any()) }

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }

    @Test
    fun `load prepend when prevKey is null returns end of pagination`() = runTest {
        val remoteKey = UpcomingRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }

    @Test
    fun `load append when nextKey is null returns end of pagination`() = runTest {
        val remoteKey = UpcomingRemoteKeyEntity(
            id = "1",
            prevKey = 0,
            nextKey = null,
            currentPage = 1,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }

    @Test
    fun `load append succeeds with more data`() = runTest {
        val remoteKey = UpcomingRemoteKeyEntity(
            id = "1",
            prevKey = 0,
            nextKey = 2,
            currentPage = 1,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        val launches = listOf(TestData.createLaunchSummary())
        val detailedResult = DetailedLaunchesResult(
            summaries = launches,
            details = listOf(TestData.createLaunch())
        )
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getUpcomingDetailedLaunches(any(), any()) } returns LaunchResult.Success(detailedResult)
        coJustRun { upcomingLaunchesLocalDataSource.appendWithKeys(any(), any(), any(), any(), any(), any()) }
        coEvery { launchDetailLocalDataSource.upsertAllLaunchDetails(any()) } returns LaunchResult.Success(Unit)

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify { upcomingLaunchesLocalDataSource.appendWithKeys(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `load refresh fails with no cached data returns error`() = runTest {
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getUpcomingDetailedLaunches(any(), any()) } returns LaunchResult.Error(Throwable("Network error"))
        coEvery { upcomingLaunchesLocalDataSource.getTotalEntries() } returns 0

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `load refresh fails with cached data available shows cache`() = runTest {
        coEvery { upcomingLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getUpcomingDetailedLaunches(any(), any()) } returns LaunchResult.Error(Throwable("Network error"))
        coEvery { upcomingLaunchesLocalDataSource.getTotalEntries() } returns 10

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    private fun createPagingState(): PagingState<Int, UpcomingLaunchEntity> {
        return PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )
    }
}
