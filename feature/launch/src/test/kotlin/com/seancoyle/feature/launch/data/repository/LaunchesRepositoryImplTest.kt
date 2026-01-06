package com.seancoyle.feature.launch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.database.entities.LaunchSummaryEntity
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.feature.launch.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

    private lateinit var underTest: LaunchesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = LaunchesRepositoryImpl(
            pagerFactory = pagerFactory,
            launchesRemoteDataSource = launchesRemoteDataSource
        )
    }

    @Test
    fun `pager returns flow of paging data with query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "Falcon")
        val launchEntity = TestData.createLaunchSummaryEntity()
        val mockPager = mockk<Pager<Int, LaunchSummaryEntity>>()
        val pagingData = PagingData.from(listOf(launchEntity))

        every { pagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pagerFactory.create(launchesQuery) }
    }

    @Test
    fun `pager returns empty flow when no data available`() = runTest {
        val launchesQuery = LaunchesQuery(query = "")
        val mockPager = mockk<Pager<Int, LaunchSummaryEntity>>()
        val pagingData = PagingData.empty<LaunchSummaryEntity>()

        every { pagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        val result = underTest.pager(launchesQuery)

        assertNotNull(result)
        assertNotNull(result.first())

        verify { pagerFactory.create(launchesQuery) }
    }

    @Test
    fun `pager creates pager with correct launch query`() = runTest {
        val launchesQuery = LaunchesQuery(query = "SpaceX")
        val mockPager = mockk<Pager<Int, LaunchSummaryEntity>>()
        val pagingData = PagingData.empty<LaunchSummaryEntity>()

        every { pagerFactory.create(launchesQuery) } returns mockPager
        every { mockPager.flow } returns flowOf(pagingData)

        underTest.pager(launchesQuery)

        verify(exactly = 1) { pagerFactory.create(launchesQuery) }
    }

    @Test
    fun `getLaunch returns success when remote data source returns launches`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.UPCOMING
        val launches = TestData.createLaunch()
        val expectedResult = LaunchResult.Success(launches)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns expectedResult

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Success)
        assertEquals(launches, result.data)
    }

    @Test
    fun `getLaunch returns error when remote data source returns error`() = runTest {
        val id = "test-id"
        val launchType = LaunchesType.PAST
        val error = RemoteError.NETWORK_UNKNOWN_ERROR
        val expectedResult = LaunchResult.Error(error)
        coEvery { launchesRemoteDataSource.getLaunch(id, launchType) } returns expectedResult

        val result = underTest.getLaunch(id, launchType)

        assertTrue(result is LaunchResult.Error)
        assertEquals(error, result.error)
    }
}
