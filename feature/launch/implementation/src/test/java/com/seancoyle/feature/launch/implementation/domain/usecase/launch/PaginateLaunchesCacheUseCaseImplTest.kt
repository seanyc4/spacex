package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaginateLaunchesCacheUseCaseImplTest {

    @MockK
    private lateinit var launchRepository: LaunchRepository

    private lateinit var underTest: PaginateLaunchesCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = PaginateLaunchesCacheUseCaseImpl(launchRepository)
    }

    @Test
    fun `invoke should emit correct launches when data is available`() = runTest {
        val year = "2024"
        val order = Order.DESC
        val status = LaunchStatus.SUCCESS
        val page = 1

        coEvery {
            launchRepository.paginateCache(
                launchYear = year,
                order = order,
                launchStatus = status,
                page = page
            )
        } returns LaunchResult.Success(launchesModel)

        val results = mutableListOf<LaunchResult<List<LaunchTypes>?, DataSourceError>>()
        underTest(
            launchYear = year,
            order = order,
            launchStatus = status,
            page = page
        ).collect { results.add(it) }

        coVerify { launchRepository.paginateCache(year, order, status, page) }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(launchesModel, (results.first() as LaunchResult.Success).data)
    }

    @Test
    fun `invoke should emit error when there is a problem accessing cache`() = runTest {
        val year = "2024"
        val order = Order.DESC
        val status = LaunchStatus.SUCCESS
        val page = 1
        val error = DataSourceError.CACHE_ERROR

        coEvery {
            launchRepository.paginateCache(
                launchYear = year,
                order = order,
                launchStatus = status,
                page = page
            )
        } returns LaunchResult.Error(error)

        val results = mutableListOf<LaunchResult<List<LaunchTypes>?, DataSourceError>>()
        underTest(
            launchYear = year,
            order = order,
            launchStatus = status,
            page = page
        ).collect { results.add(it) }

        coVerify { launchRepository.paginateCache(year, order, status, page) }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(error, (results.first() as LaunchResult.Error).error)
    }
}