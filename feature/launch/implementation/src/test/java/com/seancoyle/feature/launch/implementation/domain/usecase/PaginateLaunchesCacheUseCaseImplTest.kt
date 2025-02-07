package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaginateLaunchesCacheUseCaseImplTest {

    @MockK
    private lateinit var spaceXRepository: SpaceXRepository

    private lateinit var underTest: PaginateLaunchesCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = PaginateLaunchesCacheUseCaseImpl(spaceXRepository)
    }

    @Test
    fun `invoke should emit correct launches when data is available`() = runTest {
        val year = "2024"
        val order = Order.DESC
        val status = LaunchStatus.SUCCESS
        val page = 1

        coEvery {
            spaceXRepository.paginateLaunches(
                launchYear = year,
                order = order,
                launchStatus = status,
                page = page
            )
        } returns Result.Success(launchesModel)

        val results = mutableListOf<Result<List<LaunchTypes>?, DataError>>()
        underTest(
            launchYear = year,
            order = order,
            launchStatus = status,
            page = page
        ).collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(launchesModel, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should emit error when there is a problem accessing cache`() = runTest {
        val year = "2024"
        val order = Order.DESC
        val status = LaunchStatus.SUCCESS
        val page = 1
        val error = DataError.CACHE_ERROR

        coEvery {
            spaceXRepository.paginateLaunches(
                launchYear = year,
                order = order,
                launchStatus = status,
                page = page
            )
        } returns Result.Error(error)

        val results = mutableListOf<Result<List<LaunchTypes>?, DataError>>()
        underTest(
            launchYear = year,
            order = order,
            launchStatus = status,
            page = page
        ).collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }
}