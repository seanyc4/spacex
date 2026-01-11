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

    private lateinit var underTest: ObserveLaunchesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = ObserveLaunchesUseCase(launchesRepository)
    }

    @Test
    fun `invoke with null status returns all launches without filtering`() = runTest {
        val launches = createLaunchSummaryList(100)
        val query = LaunchesQuery(status = null)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        assertEquals(launches.size, result.size)
        assertEquals(launches, result)
    }

    @Test
    fun `invoke with status filter on empty list returns empty list`() = runTest {
        val query = LaunchesQuery(status = LaunchStatus.SUCCESS)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(emptyList()))

        val result = underTest.invoke(query).asSnapshot()

        assertTrue(result.isEmpty())
    }

}
