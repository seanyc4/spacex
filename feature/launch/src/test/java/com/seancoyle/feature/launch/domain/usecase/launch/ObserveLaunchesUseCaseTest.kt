package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.repository.LaunchRepository
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
    private lateinit var launchRepository: LaunchRepository

    private lateinit var underTest: ObserveLaunchesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = ObserveLaunchesUseCase(launchRepository)
    }

    @Test
    fun `invoke with null status returns all launches without filtering`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchQuery(status = null)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        assertEquals(launches.size, result.size)
        assertEquals(launches, result)
    }

    @Test
    fun `invoke with status GO filters and returns only GO launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchQuery(status = LaunchStatus.GO)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status == LaunchStatus.GO }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.all { it.status == LaunchStatus.GO })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status SUCCESS filters and returns only SUCCESS launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchQuery(status = LaunchStatus.SUCCESS)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status == LaunchStatus.SUCCESS }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.all { it.status == LaunchStatus.SUCCESS })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status FAILED filters and returns only FAILED launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchQuery(status = LaunchStatus.FAILED)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status == LaunchStatus.FAILED }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status == LaunchStatus.FAILED })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status TBD filters and returns only TBD launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchQuery(status = LaunchStatus.TBD)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status == LaunchStatus.TBD }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status == LaunchStatus.TBD })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status TBC filters and returns only TBC launches`() = runTest {
        val launches = createRandomLaunchList(100)
        val query = LaunchQuery(status = LaunchStatus.TBC)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status == LaunchStatus.TBC }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status == LaunchStatus.TBC })
        assertEquals(expectedLaunches, result)
    }

    @Test
    fun `invoke with status filter on empty list returns empty list`() = runTest {
        val query = LaunchQuery(status = LaunchStatus.SUCCESS)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(emptyList()))

        val result = underTest.invoke(query).asSnapshot()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke with specific query parameters delegates to repository and applies filter`() = runTest {
        val launches = createRandomLaunchList(50)
        val query = LaunchQuery(query = "Falcon", status = LaunchStatus.GO)
        every { launchRepository.pager(query) } returns flowOf(PagingData.from(launches))

        val result = underTest.invoke(query).asSnapshot()

        val expectedLaunches = launches.filter { it.status == LaunchStatus.GO }
        assertEquals(expectedLaunches.size, result.size)
        assertTrue(result.isEmpty() || result.all { it.status == LaunchStatus.GO })
        assertEquals(expectedLaunches, result)
    }
}
