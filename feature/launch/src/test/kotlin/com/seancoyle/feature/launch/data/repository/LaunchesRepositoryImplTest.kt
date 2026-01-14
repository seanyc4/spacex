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
    private lateinit var upcomingPagerFactory: UpcomingLaunchesPagerFactory

    @MockK
    private lateinit var pastPagerFactory: PastLaunchesPagerFactory

    @MockK
    private lateinit var launchesRemoteDataSource: LaunchesRemoteDataSource

    @MockK
    private lateinit var launchDetailLocalDataSource: LaunchDetailLocalDataSource

    private lateinit var underTest: LaunchesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesRepositoryImpl(
            upcomingPagerFactory = upcomingPagerFactory,
            pastPagerFactory = pastPagerFactory,
            launchesRemoteDataSource = launchesRemoteDataSource,
            launchDetailLocalDataSource = launchDetailLocalDataSource
        )
    }

    @Test
    fun `upcomingPager returns flow of paging data with query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val launchEntity = TestData.createUpcomingLaunchEntity()
        val mockPager = mockk<Pager<Int, UpcomingLaunchEntity>>()
        val pagingData = PagingData.from(listOf(launchEntity))

        every { upcomingPagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.upcomingPager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { upcomingPagerFactory.create(launchesQuery) }
    }

    @Test
    fun `pastPager returns flow of paging data with query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val launchEntity = TestData.createPastLaunchEntity()
        val mockPager = mockk<Pager<Int, PastLaunchEntity>>()
        val pagingData = PagingData.from(listOf(launchEntity))

        every { pastPagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pastPager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pastPagerFactory.create(launchesQuery) }
    }

    @Test
    fun `upcomingPager returns empty flow when no data available`() = runTest {
        val launchesQuery = LaunchesQuery(query = "")
        val mockPager = mockk<Pager<Int, UpcomingLaunchEntity>>()
        val pagingData = PagingData.empty<UpcomingLaunchEntity>()

        every { upcomingPagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.upcomingPager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { upcomingPagerFactory.create(launchesQuery) }
    }

    @Test
    fun `upcomingPager creates pager with correct launch query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val mockPager = mockk<Pager<Int, UpcomingLaunchEntity>>()
        val pagingData = PagingData.empty<UpcomingLaunchEntity>()

        every { upcomingPagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        underTest.upcomingPager(launchesQuery)

        verify(exactly = 1) { upcomingPagerFactory.create(launchesQuery) }
    }

    @Test
    fun `getLaunch returns cached data when available in local data source`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val cachedLaunch = TestData.createLaunch()
        coEvery { launchDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(cachedLaunch)

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Success)
        assertEquals(cachedLaunch, result.data)
        coVerify(exactly = 0) { launchesRemoteDataSource.getLaunch(any(), any()) }
    }

    @Test
    fun `getLaunch falls back to remote when cache returns null`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val remoteLaunch = TestData.createLaunch()
        coEvery { launchDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(null)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Success(remoteLaunch)

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Success)
        assertEquals(remoteLaunch, result.data)
        coVerify(exactly = 1) { launchesRemoteDataSource.getLaunch(id, launchType) }
    }

    @Test
    fun `getLaunch falls back to remote when cache returns error`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val remoteLaunch = TestData.createLaunch()
        coEvery { launchDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Error(Throwable("Cache error"))
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Success(remoteLaunch)

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Success)
        assertEquals(remoteLaunch, result.data)
        coVerify(exactly = 1) { launchesRemoteDataSource.getLaunch(id, launchType) }
    }

    @Test
    fun `getLaunch returns error when both cache and remote fail`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val error = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { launchDetailLocalDataSource.getLaunchDetail(id) } returns LaunchResult.Success(null)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns LaunchResult.Error(error)

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, result.error)
    }
}
