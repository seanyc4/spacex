package com.seancoyle.feature.launch.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
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
class PastLaunchesRemoteMediatorTest {

    @MockK
    private lateinit var launchesRemoteDataSource: LaunchesRemoteDataSource

    @MockK
    private lateinit var pastLaunchesLocalDataSource: LaunchesLocalDataSource<PastRemoteKeyEntity>

    @MockK
    private lateinit var detailLocalDataSource: DetailLocalDataSource

    private lateinit var launchesQuery: LaunchesQuery
    private lateinit var underTest: PastLaunchesRemoteMediator

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
        underTest = PastLaunchesRemoteMediator(
            remoteDataSource = launchesRemoteDataSource,
            launchesLocalDataSource = pastLaunchesLocalDataSource,
            detailLocalDataSource = detailLocalDataSource,
            launchesQuery = launchesQuery
        )
    }

    @Test
    fun `GIVEN no remote keys exist WHEN initialize THEN returns LAUNCH_INITIAL_REFRESH`() = runTest {
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `GIVEN cache is stale WHEN initialize THEN returns LAUNCH_INITIAL_REFRESH`() = runTest {
        val staleCreatedAt = System.currentTimeMillis() - (2 * 60 * 60 * 1000)
        val remoteKey = PastRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = staleCreatedAt,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `GIVEN cache is valid and query unchanged WHEN initialize THEN returns SKIP_INITIAL_REFRESH`() = runTest {
        val recentCreatedAt = System.currentTimeMillis()
        val remoteKey = PastRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = recentCreatedAt,
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, result)
    }

    @Test
    fun `GIVEN search query changed from cached WHEN initialize THEN returns LAUNCH_INITIAL_REFRESH`() = runTest {
        launchesQuery = LaunchesQuery(query = "Falcon")
        underTest = PastLaunchesRemoteMediator(
            remoteDataSource = launchesRemoteDataSource,
            launchesLocalDataSource = pastLaunchesLocalDataSource,
            detailLocalDataSource = detailLocalDataSource,
            launchesQuery = launchesQuery
        )

        val remoteKey = PastRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val result = underTest.initialize()

        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun `GIVEN REFRESH load type WHEN load succeeds THEN clears old data`() = runTest {
        val launches = listOf(TestData.createLaunchSummary())
        val detailedResult = DetailedLaunchesResult(
            summaries = launches,
            details = listOf(TestData.createLaunch())
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getPastDetailedLaunches(any(), any()) } returns LaunchResult.Success(detailedResult)
        coJustRun { pastLaunchesLocalDataSource.refreshWithKeys(any(), any(), any(), any(), any(), any()) }
        coJustRun { detailLocalDataSource.refreshLaunches(any()) }

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify { pastLaunchesLocalDataSource.refreshWithKeys(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `GIVEN empty response WHEN REFRESH load THEN returns end of pagination`() = runTest {
        val detailedResult = DetailedLaunchesResult(
            summaries = emptyList(),
            details = emptyList()
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getPastDetailedLaunches(any(), any()) } returns LaunchResult.Success(detailedResult)
        coJustRun { pastLaunchesLocalDataSource.refreshWithKeys(any(), any(), any(), any(), any(), any()) }
        coJustRun { detailLocalDataSource.refreshLaunches(any()) }

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }

    @Test
    fun `GIVEN prevKey is null WHEN PREPEND load THEN returns end of pagination`() = runTest {
        val remoteKey = PastRemoteKeyEntity(
            id = "1",
            prevKey = null,
            nextKey = 1,
            currentPage = 0,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.PREPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }

    @Test
    fun `GIVEN nextKey is null WHEN APPEND load THEN returns end of pagination`() = runTest {
        val remoteKey = PastRemoteKeyEntity(
            id = "1",
            prevKey = 0,
            nextKey = null,
            currentPage = 1,
            createdAt = System.currentTimeMillis(),
            cachedQuery = "",
            cachedLaunchStatus = null
        )
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue(result.endOfPaginationReached)
    }

    @Test
    fun `GIVEN more data available WHEN APPEND load THEN succeeds`() = runTest {
        val remoteKey = PastRemoteKeyEntity(
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
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns listOf(remoteKey)
        coEvery { launchesRemoteDataSource.getPastDetailedLaunches(any(), any()) } returns LaunchResult.Success(detailedResult)
        coJustRun { pastLaunchesLocalDataSource.appendWithKeys(any(), any(), any(), any(), any(), any()) }
        coEvery { detailLocalDataSource.upsertAllLaunchDetails(any()) } returns LaunchResult.Success(Unit)

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.APPEND, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify { pastLaunchesLocalDataSource.appendWithKeys(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `GIVEN network error and no cached data WHEN REFRESH load THEN returns error`() = runTest {
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getPastDetailedLaunches(any(), any()) } returns LaunchResult.Error(Throwable("Network error"))
        coEvery { pastLaunchesLocalDataSource.getTotalEntries() } returns 0

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun `GIVEN network error and cached data available WHEN REFRESH load THEN shows cache`() = runTest {
        coEvery { pastLaunchesLocalDataSource.getRemoteKeys() } returns emptyList()
        coEvery { launchesRemoteDataSource.getPastDetailedLaunches(any(), any()) } returns LaunchResult.Error(Throwable("Network error"))
        coEvery { pastLaunchesLocalDataSource.getTotalEntries() } returns 10

        val pagingState = createPagingState()
        val result = underTest.load(LoadType.REFRESH, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    private fun createPagingState(): PagingState<Int, PastLaunchEntity> {
        return PagingState(
            pages = emptyList(),
            anchorPosition = null,
            config = pagingConfig,
            leadingPlaceholderCount = 0
        )
    }
}
