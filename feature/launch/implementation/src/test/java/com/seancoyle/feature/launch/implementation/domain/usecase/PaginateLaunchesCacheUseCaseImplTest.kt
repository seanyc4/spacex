package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.util.TestData
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
        val year = "2025"
        val order = Order.DESC
        val status = LaunchStatus.UNKNOWN
        val page = 1
        val launches = listOf(TestData.createLaunch())

        coEvery {
            launchRepository.paginateCache(
                launchYear = year,
                order = order,
                launchStatus = status,
                page = page
            )
        } returns LaunchResult.Success(launches)

        val results = mutableListOf<LaunchResult<List<LaunchTypes>, LocalError>>()
        underTest(
            launchYear = year,
            order = order,
            launchStatus = status,
            page = page
        ).collect { results.add(it) }

        coVerify { launchRepository.paginateCache(year, order, status, page) }

        assertTrue(results.first() is LaunchResult.Success)
        val data = (results.first() as LaunchResult.Success).data
        assertEquals(1, data.size)
        assertEquals("faf4a0bc-7dad-4842-b74c-73a9f648b5cc", (data[0] as LaunchTypes.Launch).id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", (data[0] as LaunchTypes.Launch).name)
    }

    @Test
    fun `invoke should emit error when there is a problem accessing cache`() = runTest {
        val year = "2025"
        val order = Order.DESC
        val status = LaunchStatus.UNKNOWN
        val page = 1
        val error = LocalError.CACHE_ERROR

        coEvery {
            launchRepository.paginateCache(
                launchYear = year,
                order = order,
                launchStatus = status,
                page = page
            )
        } returns LaunchResult.Error(error)

        val results = mutableListOf<LaunchResult<List<LaunchTypes>, LocalError>>()
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
