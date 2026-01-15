package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.util.TestData.createLaunchSummaryList
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ObserveLaunchesUseCaseTest {

    @MockK
    private lateinit var launchesRepository: LaunchesRepository

    private lateinit var observeUpcomingLaunchesUseCase: ObserveUpcomingLaunchesUseCase
    private lateinit var observePastLaunchesUseCase: ObservePastLaunchesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        observeUpcomingLaunchesUseCase = ObserveUpcomingLaunchesUseCase(launchesRepository)
        observePastLaunchesUseCase = ObservePastLaunchesUseCase(launchesRepository)
    }

    @Test
    fun `upcoming with null status returns all upcoming launches without filtering`() = runTest {
        val launches = createLaunchSummaryList(100)
        val query = LaunchesQuery(status = null)
        every { launchesRepository.upcomingPager(query) } returns flowOf(PagingData.from(launches))

        val result = observeUpcomingLaunchesUseCase(query).asSnapshot()

        assertEquals(launches.size, result.size)
        assertEquals(launches, result)
    }

    @Test
    fun `past with status filter on empty list returns empty list`() = runTest {
        val query = LaunchesQuery(status = LaunchStatus.SUCCESS)
        every { launchesRepository.pastPager(query) } returns flowOf(PagingData.from(emptyList()))

        val result = observePastLaunchesUseCase(query).asSnapshot()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `upcoming returns flow from repository upcomingPager`() = runTest {
        val launches = createLaunchSummaryList(50)
        val query = LaunchesQuery()
        every { launchesRepository.upcomingPager(query) } returns flowOf(PagingData.from(launches))

        val result = observeUpcomingLaunchesUseCase(query).asSnapshot()

        assertEquals(50, result.size)
    }

    @Test
    fun `past returns flow from repository pastPager`() = runTest {
        val launches = createLaunchSummaryList(50)
        val query = LaunchesQuery()
        every { launchesRepository.pastPager(query) } returns flowOf(PagingData.from(launches))

        val result = observePastLaunchesUseCase(query).asSnapshot()

        assertEquals(50, result.size)
    }
}
