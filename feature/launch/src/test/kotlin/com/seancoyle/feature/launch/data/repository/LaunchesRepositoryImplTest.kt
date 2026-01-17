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
    fun `upcomingPager returns flow of paging data with query`() = runTest {
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
    fun `pastPager returns flow of paging data with query`() = runTest {
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
    fun `upcomingPager returns empty flow when no data available`() = runTest {
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
    fun `upcomingPager creates pager with correct launch query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val mockPager: Pager<Int, UpcomingLaunchEntity> = mockk()
        val pagingData = PagingData.empty<UpcomingLaunchEntity>()

        every { pagerFactory.createUpcoming(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        underTest.upcomingPager(launchesQuery).first()

        verify(exactly = 1) { pagerFactory.createUpcoming(launchesQuery) }
    }

    @Test
    fun `getLaunch returns cached data when available in UPCOMING detail cache`() = runTest {
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
    fun `getLaunch returns cached data when available in PAST detail cache`() = runTest {
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
    fun `getLaunch falls back to remote when upcoming cache returns null`() = runTest {
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
    fun `getLaunch falls back to remote when past cache returns error`() = runTest {
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
    fun `getLaunch returns error when both cache and remote fail`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val error = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { pastDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(null)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Error(error)

        val result = underTest.getLaunch(id, launchType, isRefresh = false)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, result.error)
    }
}
