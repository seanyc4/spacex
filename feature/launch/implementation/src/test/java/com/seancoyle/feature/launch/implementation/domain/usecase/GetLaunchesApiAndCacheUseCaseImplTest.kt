package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.util.TestData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetLaunchesApiAndCacheUseCaseImplTest {

    @MockK
    private lateinit var dateFormatter: DateFormatter

    @MockK
    private lateinit var dateTransformer: DateTransformer

    @MockK
    private lateinit var launchRepository: LaunchRepository

    private lateinit var underTest: GetLaunchesApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchesApiAndCacheUseCaseImpl(
            launchRepository = launchRepository,
            dateFormatter = dateFormatter,
            dateTransformer = dateTransformer
        )
    }

    @Test
    fun `invoke should emit success result when network and cache operations are successful`() = runTest {
        val currentPage = 0
        val offset = 0
        val expectedLaunchDate = "2025-12-13T05:34:00Z"
        val launch = TestData.createLaunch()
        val launches = listOf(launch)
        val launchDateTime = launch.launchDateLocalDateTime!!

        coEvery { launchRepository.getLaunchesApi(offset) } returns LaunchResult.Success(launches)
        coEvery { launchRepository.insertLaunchesCache(any()) } returns LaunchResult.Success(Unit)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchDateTime
        every { dateTransformer.formatDateTimeToString(launchDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchDateTime) } returns "2025"
        every { dateTransformer.getLaunchDaysDifference(launchDateTime) } returns "0 days"
        every { dateTransformer.isPastLaunch(launchDateTime) } returns false

        val results = mutableListOf<LaunchResult<List<com.seancoyle.feature.launch.api.domain.model.LaunchTypes.Launch>, DataError>>()
        underTest(currentPage).collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(offset)
            launchRepository.insertLaunchesCache(any())
        }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchDateTime)
            dateTransformer.returnYearOfLaunch(launchDateTime)
            dateTransformer.getLaunchDaysDifference(launchDateTime)
            dateTransformer.isPastLaunch(launchDateTime)
        }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(1, (results.first() as LaunchResult.Success).data.size)
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", (results.first() as LaunchResult.Success).data[0].id)
    }

    @Test
    fun `invoke should emit error when network fetch fails`() = runTest {
        val currentPage = 0
        val offset = 0
        val networkError = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { launchRepository.getLaunchesApi(offset) } returns LaunchResult.Error(networkError)

        val results = mutableListOf<LaunchResult<List<com.seancoyle.feature.launch.api.domain.model.LaunchTypes.Launch>, DataError>>()
        underTest(currentPage).collect { results.add(it) }

        coVerify { launchRepository.getLaunchesApi(offset) }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(networkError, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit error when cache operation fails`() = runTest {
        val currentPage = 0
        val offset = 0
        val expectedLaunchDate = "2025-12-13T05:34:00Z"
        val launch = TestData.createLaunch()
        val launches = listOf(launch)
        val launchDateTime = launch.launchDateLocalDateTime!!
        val cacheError = LocalError.CACHE_ERROR
        coEvery { launchRepository.getLaunchesApi(offset) } returns LaunchResult.Success(launches)
        coEvery { launchRepository.insertLaunchesCache(any()) } returns LaunchResult.Error(cacheError)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchDateTime
        every { dateTransformer.formatDateTimeToString(launchDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchDateTime) } returns "2025"
        every { dateTransformer.getLaunchDaysDifference(launchDateTime) } returns "0 days"
        every { dateTransformer.isPastLaunch(launchDateTime) } returns false

        val results = mutableListOf<LaunchResult<List<com.seancoyle.feature.launch.api.domain.model.LaunchTypes.Launch>, DataError>>()
        underTest(currentPage).collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(offset)
            launchRepository.insertLaunchesCache(any())
        }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchDateTime)
            dateTransformer.returnYearOfLaunch(launchDateTime)
            dateTransformer.getLaunchDaysDifference(launchDateTime)
            dateTransformer.isPastLaunch(launchDateTime)
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(cacheError, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit success when network returns empty list`() = runTest {
        val currentPage = 0
        val offset = 0
        coEvery { launchRepository.getLaunchesApi(offset) } returns LaunchResult.Success(emptyList())
        coEvery { launchRepository.insertLaunchesCache(emptyList()) } returns LaunchResult.Success(Unit)

        val results = mutableListOf<LaunchResult<List<com.seancoyle.feature.launch.api.domain.model.LaunchTypes.Launch>, DataError>>()
        underTest(currentPage).collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(offset)
            launchRepository.insertLaunchesCache(emptyList())
        }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(0, (results.first() as LaunchResult.Success).data.size)
    }

    @Test
    fun `invoke should transform launch data correctly before caching`() = runTest {
        val currentPage = 0
        val offset = 0
        val expectedLaunchDate = "2025-12-13T05:34:00Z"
        val launch = TestData.createLaunch()
        val launches = listOf(launch)
        val launchDateTime = launch.launchDateLocalDateTime!!
        coEvery { launchRepository.getLaunchesApi(offset) } returns LaunchResult.Success(launches)
        coEvery { launchRepository.insertLaunchesCache(any()) } returns LaunchResult.Success(Unit)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchDateTime
        every { dateTransformer.formatDateTimeToString(launchDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchDateTime) } returns "2025"
        every { dateTransformer.getLaunchDaysDifference(launchDateTime) } returns "0 days"
        every { dateTransformer.isPastLaunch(launchDateTime) } returns false

        val results = mutableListOf<LaunchResult<List<com.seancoyle.feature.launch.api.domain.model.LaunchTypes.Launch>, DataError>>()
        underTest(currentPage).collect { results.add(it) }

        coVerify { launchRepository.insertLaunchesCache(any()) }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchDateTime)
            dateTransformer.returnYearOfLaunch(launchDateTime)
            dateTransformer.getLaunchDaysDifference(launchDateTime)
            dateTransformer.isPastLaunch(launchDateTime)
        }

        assertTrue(results.first() is LaunchResult.Success)
        val transformedLaunches = (results.first() as LaunchResult.Success).data
        assertEquals(expectedLaunchDate, transformedLaunches.first().launchDate)
        assertEquals("2025", transformedLaunches.first().launchYear)
        assertEquals("0 days", transformedLaunches.first().launchDays)
    }

    @Test
    fun `invoke should return error if transformation succeeds but cache insert fails`() = runTest {
        val currentPage = 0
        val offset = 0
        val expectedLaunchDate = "2025-12-13T05:34:00Z"
        val launch = TestData.createLaunch()
        val launches = listOf(launch)
        val launchDateTime = launch.launchDateLocalDateTime!!
        val cacheError = LocalError.CACHE_ERROR
        coEvery { launchRepository.getLaunchesApi(offset) } returns LaunchResult.Success(launches)
        coEvery { launchRepository.insertLaunchesCache(any()) } returns LaunchResult.Error(cacheError)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchDateTime
        every { dateTransformer.formatDateTimeToString(launchDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchDateTime) } returns "2025"
        every { dateTransformer.getLaunchDaysDifference(launchDateTime) } returns "0 days"
        every { dateTransformer.isPastLaunch(launchDateTime) } returns false

        val results = mutableListOf<LaunchResult<List<com.seancoyle.feature.launch.api.domain.model.LaunchTypes.Launch>, DataError>>()
        underTest(currentPage).collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(offset)
            launchRepository.insertLaunchesCache(any())
        }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchDateTime)
            dateTransformer.returnYearOfLaunch(launchDateTime)
            dateTransformer.getLaunchDaysDifference(launchDateTime)
            dateTransformer.isPastLaunch(launchDateTime)
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(cacheError, (results.first() as LaunchResult.Error).error)
    }

}
