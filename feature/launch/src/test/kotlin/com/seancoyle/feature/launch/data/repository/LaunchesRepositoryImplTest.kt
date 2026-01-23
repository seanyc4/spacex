package com.seancoyle.feature.launch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LaunchesRepositoryImplTest {

    @MockK
    private lateinit var pagerFactory: LaunchesPagerFactory

    @MockK
    private lateinit var launchesRemoteDataSource: LaunchesRemoteDataSource

    @MockK
    private lateinit var upcomingDetailLocalDataSource: DetailLocalDataSource

    @MockK
    private lateinit var pastDetailLocalDataSource: DetailLocalDataSource

    private lateinit var underTest: LaunchesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesRepositoryImpl(
            pagerFactory = pagerFactory,
            launchesRemoteDataSource = launchesRemoteDataSource,
            upcomingDetailLocalDataSource = upcomingDetailLocalDataSource,
            pastDetailLocalDataSource = pastDetailLocalDataSource
        )
    }

    @Test
    fun `GIVEN query WHEN upcomingPager called THEN returns flow of paging data`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val launchEntity = TestData.createUpcomingLaunchEntity()
        val mockPager: Pager<Int, UpcomingLaunchEntity> = mockk()
        val pagingData = PagingData.from(listOf(launchEntity))
        every { pagerFactory.createUpcoming(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.upcomingPager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())
        verify { pagerFactory.createUpcoming(launchesQuery) }
    }

    @Test
    fun `GIVEN query WHEN pastPager called THEN returns flow of paging data`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val launchEntity = TestData.createPastLaunchEntity()
        val mockPager: Pager<Int, PastLaunchEntity> = mockk()
        val pagingData = PagingData.from(listOf(launchEntity))
        every { pagerFactory.createPast(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pastPager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())
        verify { pagerFactory.createPast(launchesQuery) }
    }

    @Test
    fun `GIVEN empty query WHEN upcomingPager called THEN returns empty flow`() = runTest {
        val launchesQuery = LaunchesQuery(query = "")
        val mockPager: Pager<Int, UpcomingLaunchEntity> = mockk()
        val pagingData = PagingData.empty<UpcomingLaunchEntity>()
        every { pagerFactory.createUpcoming(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.upcomingPager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())
        verify { pagerFactory.createUpcoming(launchesQuery) }
    }

    @Test
    fun `GIVEN query WHEN upcomingPager called THEN pager created with correct query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val mockPager: Pager<Int, UpcomingLaunchEntity> = mockk()
        val pagingData = PagingData.empty<UpcomingLaunchEntity>()
        every { pagerFactory.createUpcoming(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        underTest.upcomingPager(launchesQuery).first()

        verify(exactly = 1) { pagerFactory.createUpcoming(launchesQuery) }
    }

    @Test
    fun `GIVEN cached data exists for UPCOMING WHEN getLaunch with isRefresh false THEN returns cached data`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val cachedLaunch = TestData.createLaunch()
        coEvery { upcomingDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)

        val result = underTest.getLaunch(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertEquals(cachedLaunch, result.data)
        coVerify(exactly = 0) { launchesRemoteDataSource.getLaunch(any(), any()) }
    }

    @Test
    fun `GIVEN cached data exists for PAST WHEN getLaunch with isRefresh false THEN returns cached data`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val cachedLaunch = TestData.createLaunch()
        coEvery { pastDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)

        val result = underTest.getLaunch(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertEquals(cachedLaunch, result.data)
        coVerify(exactly = 0) { launchesRemoteDataSource.getLaunch(any(), any()) }
    }

    @Test
    fun `GIVEN cache returns null WHEN getLaunch with isRefresh false THEN fetches from network`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val remoteLaunch = TestData.createLaunch()
        coEvery { upcomingDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(null)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Success(remoteLaunch)

        val result = underTest.getLaunch(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertEquals(remoteLaunch, result.data)
        coVerify(exactly = 1) { launchesRemoteDataSource.getLaunch(id, launchType) }
    }

    @Test
    fun `GIVEN cache returns error WHEN getLaunch with isRefresh false THEN fetches from network`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val remoteLaunch = TestData.createLaunch()
        coEvery { pastDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Error(Throwable("Cache error"))
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Success(remoteLaunch)

        val result = underTest.getLaunch(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Success)
        assertEquals(remoteLaunch, result.data)
        coVerify(exactly = 1) { launchesRemoteDataSource.getLaunch(id, launchType) }
    }

    @Test
    fun `GIVEN cache and network fail WHEN getLaunch THEN returns error`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val error = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { pastDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(null)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Error(error)

        val result = underTest.getLaunch(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, result.error)
    }

    @Test
    fun `GIVEN isRefresh true WHEN getLaunch for UPCOMING THEN bypasses cache and fetches from network`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val cachedLaunch = TestData.createLaunch(id = "cached-id")
        val remoteLaunch = TestData.createLaunch(id = "remote-id")
        coEvery { upcomingDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Success(remoteLaunch)

        val result = underTest.getLaunch(id, launchType, isRefresh = true)

        assertTrue(result is LaunchResult.Success)
        assertEquals(remoteLaunch, result.data)
        coVerify(exactly = 0) { upcomingDetailLocalDataSource.getLaunchDetail(any()) }
        coVerify(exactly = 1) { launchesRemoteDataSource.getLaunch(id, launchType) }
    }

    @Test
    fun `GIVEN isRefresh true WHEN getLaunch for PAST THEN bypasses cache and fetches from network`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val cachedLaunch = TestData.createLaunch(id = "cached-id")
        val remoteLaunch = TestData.createLaunch(id = "remote-id")
        coEvery { pastDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Success(remoteLaunch)

        val result = underTest.getLaunch(id, launchType, isRefresh = true)

        assertTrue(result is LaunchResult.Success)
        assertEquals(remoteLaunch, result.data)
        coVerify(exactly = 0) { pastDetailLocalDataSource.getLaunchDetail(any()) }
        coVerify(exactly = 1) { launchesRemoteDataSource.getLaunch(id, launchType) }
    }

    @Test
    fun `GIVEN isRefresh true and network fails WHEN getLaunch THEN returns network error`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val error = RemoteError.NETWORK_CONNECTION_FAILED
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Error(error)

        val result = underTest.getLaunch(id, launchType, isRefresh = true)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, result.error)
        coVerify(exactly = 0) { upcomingDetailLocalDataSource.getLaunchDetail(any()) }
    }

    @Test
    fun `GIVEN UPCOMING type WHEN getLaunch THEN uses upcomingDetailLocalDataSource`() = runTest {
        val id = "test-id"
        val cachedLaunch = TestData.createLaunch()
        coEvery { upcomingDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)

        underTest.getLaunch(id, LaunchesType.UPCOMING, isRefresh = false)

        coVerify(exactly = 1) { upcomingDetailLocalDataSource.getLaunchDetail(id) }
        coVerify(exactly = 0) { pastDetailLocalDataSource.getLaunchDetail(any()) }
    }

    @Test
    fun `GIVEN PAST type WHEN getLaunch THEN uses pastDetailLocalDataSource`() = runTest {
        val id = "test-id"
        val cachedLaunch = TestData.createLaunch()
        coEvery { pastDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)

        underTest.getLaunch(id, LaunchesType.PAST, isRefresh = false)

        coVerify(exactly = 1) { pastDetailLocalDataSource.getLaunchDetail(id) }
        coVerify(exactly = 0) { upcomingDetailLocalDataSource.getLaunchDetail(any()) }
    }
}
