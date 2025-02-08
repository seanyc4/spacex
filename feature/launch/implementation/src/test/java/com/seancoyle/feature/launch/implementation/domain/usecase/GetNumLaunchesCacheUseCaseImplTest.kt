package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetNumLaunchesCacheUseCaseImplTest {

    @MockK
    private lateinit var launchRepository: LaunchRepository

    private lateinit var underTest: GetNumLaunchesCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetNumLaunchesCacheUseCaseImpl(launchRepository)
    }

    @Test
    fun `invoke should emit the correct number of entries when data is available`() = runTest {
        val totalEntries = 42
        coEvery { launchRepository.getTotalEntries() } returns Result.Success(totalEntries)

        val results = mutableListOf<Result<Int?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(totalEntries, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should emit error when no entries are found`() = runTest {
        coEvery { launchRepository.getTotalEntries() } returns Result.Error(DataError.CACHE_DATA_NULL)

        val results = mutableListOf<Result<Int?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(DataError.CACHE_DATA_NULL, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should emit an error when there is a problem accessing cache`() = runTest {
        val error = DataError.CACHE_ERROR
        coEvery { launchRepository.getTotalEntries() } returns Result.Error(error)

        val results = mutableListOf<Result<Int?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }
}