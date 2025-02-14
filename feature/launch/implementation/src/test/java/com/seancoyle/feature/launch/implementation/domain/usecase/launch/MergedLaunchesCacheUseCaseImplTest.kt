package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.numberformatter.NumberFormatter
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.CAROUSEL
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.GRID
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.HEADER
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.LIST
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyCacheUseCase
import com.seancoyle.feature.launch.implementation.util.TestData.carouselModel
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import com.seancoyle.feature.launch.implementation.util.TestData.gridModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class MergedLaunchesCacheUseCaseImplTest {

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var getCompanyFromCacheUseCase: GetCompanyCacheUseCase

    @MockK
    private lateinit var getLaunchesFromCacheUseCase: PaginateLaunchesCacheUseCase

    @MockK
    private lateinit var numberFormatter: NumberFormatter

    private lateinit var underTest: MergedLaunchesCacheUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = MergedLaunchesCacheUseCaseImpl(
            getCompanyFromCacheUseCase = getCompanyFromCacheUseCase,
            getLaunchesFromCacheUseCase = getLaunchesFromCacheUseCase,
            numberFormatter = numberFormatter
        )
    }

    @Test
    fun `invoke should emit merged results when both company and launches are available`() = runTest {
        val companyResult: Flow<LaunchResult<Company, DataSourceError>> = flowOf(LaunchResult.Success(companyModel))
        val launchesResult: Flow<LaunchResult<List<LaunchTypes>, DataSourceError>> = flowOf(LaunchResult.Success(launchesModel))

        coEvery { getCompanyFromCacheUseCase() } returns companyResult
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult
        every { numberFormatter.formatNumber(100) } returns "100"
        every { numberFormatter.formatNumber(74000000000) } returns "74,000,000,000"

        val results = mutableListOf<LaunchResult<List<LaunchTypes>, DataSourceError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        coVerify {
            getCompanyFromCacheUseCase()
            getLaunchesFromCacheUseCase("2024", Order.DESC, LaunchStatus.ALL, 1)
        }

        verify {
            numberFormatter.formatNumber(100)
            numberFormatter.formatNumber(74000000000)
        }

        assertTrue(results.first() is LaunchResult.Success)
        val data = (results.first() as LaunchResult.Success).data
        assertNotNull(data)
        assertTrue(data.size > 1)
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == HEADER })
        assertTrue(data.any { it is LaunchTypes.CompanySummary && it.summary == "" })
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == CAROUSEL })
        assertTrue(data.any { it is LaunchTypes.Carousel && it.items.all { item -> carouselModel.items.contains(item) } })
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == GRID })
        assertTrue(data.any { it is LaunchTypes.Grid && it.items == gridModel.items })
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == LIST })
        assertTrue(data.any { it is LaunchTypes.Launch && listOf(it).all { item -> launchesModel.contains(item) } })
    }

    @Test
    fun `invoke should emit error when there is an error fetching company`() = runTest {
        val error = DataSourceError.CACHE_ERROR
        val launches = listOf<LaunchTypes>()
        val companyResult: Flow<LaunchResult<Company?, DataSourceError>> = flowOf(LaunchResult.Error(error))
        val launchesResult: Flow<LaunchResult<List<LaunchTypes>, DataSourceError>> = flowOf(LaunchResult.Success(launches))

        coEvery { getCompanyFromCacheUseCase() } returns companyResult
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<LaunchResult<List<LaunchTypes>, DataSourceError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        coVerify {
            getCompanyFromCacheUseCase()
            getLaunchesFromCacheUseCase(any(), any(), any(), any())
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(error, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit error when no launches are found`() = runTest {
        val companyResult: Flow<LaunchResult<Company, DataSourceError>> = flowOf(LaunchResult.Success(companyModel))
        val launchesResult: Flow<LaunchResult<List<LaunchTypes>, DataSourceError>> = flowOf(LaunchResult.Error(DataSourceError.CACHE_ERROR_NO_RESULTS))

        coEvery { getCompanyFromCacheUseCase() } returns companyResult
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<LaunchResult<List<LaunchTypes>, DataSourceError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        coVerify {
            getCompanyFromCacheUseCase()
            getLaunchesFromCacheUseCase(any(), any(), any(), any())
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(DataSourceError.CACHE_ERROR_NO_RESULTS, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit error when network issues occur fetching company`() = runTest {
        val error = DataSourceError.NETWORK_CONNECTION_FAILED
        val launchesResult: Flow<LaunchResult<List<LaunchTypes>, DataSourceError>> = flowOf(LaunchResult.Success(launchesModel))

        coEvery { getCompanyFromCacheUseCase() } returns flowOf(LaunchResult.Error(error))
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<LaunchResult<List<LaunchTypes>, DataSourceError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        coVerify {
            getCompanyFromCacheUseCase()
            getLaunchesFromCacheUseCase(any(), any(), any(), any())
        }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(error, (results.first() as LaunchResult.Error).error)
    }

}