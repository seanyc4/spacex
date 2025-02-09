package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.CAROUSEL
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.GRID
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.HEADER
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl.Companion.LIST
import com.seancoyle.feature.launch.implementation.util.TestData.carouselModel
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import com.seancoyle.feature.launch.implementation.util.TestData.gridModel
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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

    private lateinit var underTest: MergedLaunchesCacheUseCaseImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        underTest = MergedLaunchesCacheUseCaseImpl(
            getCompanyFromCacheUseCase = getCompanyFromCacheUseCase,
            getLaunchesFromCacheUseCase = getLaunchesFromCacheUseCase
        )
    }

    @Test
    fun `invoke should emit merged results when both company and launches are available`() = runTest {
        val companyResult: Flow<Result<Company, DataError>> = flowOf(Result.Success(companyModel))
        val launchesResult: Flow<Result<List<LaunchTypes>, DataError>> = flowOf(Result.Success(launchesModel))

        coEvery { getCompanyFromCacheUseCase() } returns companyResult
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<Result<List<LaunchTypes>, DataError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        assertTrue(results.first() is Result.Success)
        val data = (results.first() as Result.Success).data
        assertNotNull(data)
        assertTrue(data.size > 1)
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == HEADER })
        assertTrue(data.any { it is LaunchTypes.CompanySummary && it.summary == companyModel })
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == CAROUSEL })
        assertTrue(data.any { it is LaunchTypes.Carousel && it.items.all { item -> carouselModel.items.contains(item) } })
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == GRID })
        assertTrue(data.any { it is LaunchTypes.Grid && it.items == gridModel.items })
        assertTrue(data.any { it is LaunchTypes.SectionTitle && it.title == LIST })
        assertTrue(data.any { it is LaunchTypes.Launch && listOf(it).all { item -> launchesModel.contains(item) } })
    }

    @Test
    fun `invoke should emit error when there is an error fetching company`() = runTest {
        val error = DataError.CACHE_ERROR
        val launches = listOf<LaunchTypes>()
        val companyResult: Flow<Result<Company?, DataError>> = flowOf(Result.Error(error))
        val launchesResult: Flow<Result<List<LaunchTypes>, DataError>> = flowOf(Result.Success(launches))

        coEvery { getCompanyFromCacheUseCase() } returns companyResult
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<Result<List<LaunchTypes>, DataError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should emit error when no launches are found`() = runTest {
        val companyResult: Flow<Result<Company, DataError>> = flowOf(Result.Success(companyModel))
        val launchesResult: Flow<Result<List<LaunchTypes>, DataError>> = flowOf(Result.Error(DataError.CACHE_ERROR_NO_RESULTS))

        coEvery { getCompanyFromCacheUseCase() } returns companyResult
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<Result<List<LaunchTypes>, DataError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        assertTrue(results.first() is Result.Error)
        assertEquals(DataError.CACHE_ERROR_NO_RESULTS, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should emit error when network issues occur fetching company`() = runTest {
        val error = DataError.NETWORK_CONNECTION_FAILED
        val launchesResult: Flow<Result<List<LaunchTypes>, DataError>> = flowOf(Result.Success(launchesModel))

        coEvery { getCompanyFromCacheUseCase() } returns flowOf(Result.Error(error))
        coEvery { getLaunchesFromCacheUseCase(any(), any(), any(), any()) } returns launchesResult

        val results = mutableListOf<Result<List<LaunchTypes>, DataError>>()

        underTest(
            year = "2024",
            order = Order.DESC,
            launchFilter = LaunchStatus.ALL,
            page = 1
        ).collect {
            results.add(it)
        }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }

}