package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.util.TestData.launchModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
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

    @MockK
    private lateinit var launchOptions: LaunchOptions

    private lateinit var underTest: GetLaunchesApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetLaunchesApiAndCacheUseCaseImpl(
            launchRepository = launchRepository,
            launchOptions = launchOptions,
            dateFormatter = dateFormatter,
            dateTransformer = dateTransformer
        )
    }

    @Test
    fun `invoke should emit success result when network and cache operations are successful`() = runTest {
        val expectedLaunchDate = "2024-01-01"
        coEvery { launchRepository.getLaunchesApi(launchOptions) } returns LaunchResult.Success(launchesModel)
        coEvery { launchRepository.insertLaunchesCache(launchesModel) } returns LaunchResult.Success(Unit)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchModel.launchDateLocalDateTime
        every { dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime) } returns "2024"
        every { dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime) } returns "5 days"
        every { dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime) } returns false

        val results = mutableListOf<LaunchResult<Unit, DataSourceError>>()
        underTest().collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(launchOptions)
            launchRepository.insertLaunchesCache(launchesModel)
        }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime)
            dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime)
            dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime)
            dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime)
        }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(Unit, (results.first() as LaunchResult.Success).data)
    }

    @Test
    fun `invoke should emit error when network fetch fails`() = runTest {
        val networkError = DataSourceError.NETWORK_UNKNOWN_ERROR
        coEvery { launchRepository.getLaunchesApi(launchOptions) } returns LaunchResult.Error(networkError)

        val results = mutableListOf<LaunchResult<Unit, DataSourceError>>()
        underTest().collect { results.add(it) }

        coVerify { launchRepository.getLaunchesApi(launchOptions) }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(networkError, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit error when cache operation fails`() = runTest {
        val expectedLaunchDate = "2024-01-01"
        val cacheError = DataSourceError.CACHE_ERROR
        coEvery { launchRepository.getLaunchesApi(launchOptions) } returns LaunchResult.Success(launchesModel)
        coEvery { launchRepository.insertLaunchesCache(launchesModel) } returns LaunchResult.Error(cacheError)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchModel.launchDateLocalDateTime
        every { dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime) } returns "2024"
        every { dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime) } returns "5 days"
        every { dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime) } returns false

        val results = mutableListOf<LaunchResult<Unit, DataSourceError>>()
        underTest().collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(launchOptions)
            launchRepository.insertLaunchesCache(launchesModel)
        }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime)
            dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime)
            dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime)
            dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime)
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(cacheError, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit success when network returns empty list`() = runTest {
        coEvery { launchRepository.getLaunchesApi(launchOptions) } returns LaunchResult.Success(emptyList())
        coEvery { launchRepository.insertLaunchesCache(emptyList()) } returns LaunchResult.Success(Unit)

        val results = mutableListOf<LaunchResult<Unit, DataSourceError>>()
        underTest().collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(launchOptions)
            launchRepository.insertLaunchesCache(emptyList())
        }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(Unit, (results.first() as LaunchResult.Success).data)
    }

    @Test
    fun `invoke should transform launch data correctly before caching`() = runTest {
        val expectedLaunchDate = "2024-01-01"
        coEvery { launchRepository.getLaunchesApi(launchOptions) } returns LaunchResult.Success(launchesModel)
        coEvery { launchRepository.insertLaunchesCache(any()) } returns LaunchResult.Success(Unit)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchModel.launchDateLocalDateTime
        every { dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime) } returns "2024"
        every { dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime) } returns "5 days"
        every { dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime) } returns false

        val results = mutableListOf<LaunchResult<Unit, DataSourceError>>()
        underTest().collect { results.add(it) }

        coVerify { launchRepository.insertLaunchesCache(withArg { transformedLaunches ->
            assertEquals(expectedLaunchDate, transformedLaunches.first().launchDate)
            assertEquals("2024", transformedLaunches.first().launchYear)
            assertEquals("5 days", transformedLaunches.first().launchDays)
        }) }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime)
            dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime)
            dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime)
            dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime)
        }

        assertTrue(results.first() is LaunchResult.Success)
    }

    @Test
    fun `invoke should return error if transformation succeeds but cache insert fails`() = runTest {
        val expectedLaunchDate = "2024-01-01"
        val cacheError = DataSourceError.CACHE_ERROR
        coEvery { launchRepository.getLaunchesApi(launchOptions) } returns LaunchResult.Success(launchesModel)
        coEvery { launchRepository.insertLaunchesCache(any()) } returns LaunchResult.Error(cacheError)
        every { dateFormatter.formatDate(expectedLaunchDate) } returns launchModel.launchDateLocalDateTime
        every { dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime) } returns expectedLaunchDate
        every { dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime) } returns "2024"
        every { dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime) } returns "5 days"
        every { dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime) } returns false

        val results = mutableListOf<LaunchResult<Unit, DataSourceError>>()
        underTest().collect { results.add(it) }

        coVerify {
            launchRepository.getLaunchesApi(launchOptions)
            launchRepository.insertLaunchesCache(launchesModel)
        }

        verify {
            dateFormatter.formatDate(expectedLaunchDate)
            dateTransformer.formatDateTimeToString(launchModel.launchDateLocalDateTime)
            dateTransformer.returnYearOfLaunch(launchModel.launchDateLocalDateTime)
            dateTransformer.getLaunchDaysDifference(launchModel.launchDateLocalDateTime)
            dateTransformer.isPastLaunch(launchModel.launchDateLocalDateTime)
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(cacheError, (results.first() as LaunchResult.Error).error)
    }

}