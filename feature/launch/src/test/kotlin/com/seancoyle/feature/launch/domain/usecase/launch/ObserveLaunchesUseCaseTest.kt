package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.util.TestData.createRandomLaunchList
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
        val launches = createRandomLaunchList(100)
        val query = LaunchesQuery(status = null)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        assertEquals(launches.size, result.size)
        assertEquals(launches, result)
    }

    @Test
    fun `invoke with status GO filters and returns only GO launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchesQuery(status = LaunchStatus.GO)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status?.abbrev == LaunchStatus.GO.abbrev }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.all { it.status?.abbrev == LaunchStatus.GO.abbrev })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status SUCCESS filters and returns only SUCCESS launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchesQuery(status = LaunchStatus.SUCCESS)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status?.abbrev == LaunchStatus.SUCCESS.abbrev }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.all { it.status?.abbrev == LaunchStatus.SUCCESS.abbrev })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status FAILED filters and returns only FAILED launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchesQuery(status = LaunchStatus.FAILED)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status?.abbrev == LaunchStatus.FAILED.abbrev }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status?.abbrev == LaunchStatus.FAILED.abbrev })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status TBD filters and returns only TBD launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchesQuery(status = LaunchStatus.TBD)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status?.abbrev == LaunchStatus.TBD.abbrev }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status?.abbrev == LaunchStatus.TBD.abbrev })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status TBC filters and returns only TBC launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchesQuery(status = LaunchStatus.TBC)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status?.abbrev == LaunchStatus.TBC.abbrev }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status?.abbrev == LaunchStatus.TBC.abbrev })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status filter on empty list returns empty list`() = runTest {
        val query = LaunchesQuery(status = LaunchStatus.SUCCESS)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(emptyList()))

        val result = underTest.invoke(query).asSnapshot()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke with specific query parameters delegates to repository and applies filter`() = runTest {
        val launches = createRandomLaunchList(50)
        val query = LaunchesQuery(query = "Falcon", status = LaunchStatus.GO)
        every { launchesRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status?.abbrev == LaunchStatus.GO.abbrev }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status?.abbrev == LaunchStatus.GO.abbrev })
        assertEquals(expectedLaunches, result)
    }
}
