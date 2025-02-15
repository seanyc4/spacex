package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
        coEvery { launchRepository.getTotalEntriesCache() } returns LaunchResult.Success(totalEntries)

        val results = mutableListOf<LaunchResult<Int?, LocalError>>()
        underTest().collect { results.add(it) }

        coVerify { launchRepository.getTotalEntriesCache() }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(totalEntries, (results.first() as LaunchResult.Success).data)
    }

    @Test
    fun `invoke should emit error when no entries are found`() = runTest {
        coEvery { launchRepository.getTotalEntriesCache() } returns LaunchResult.Error(LocalError.CACHE_DATA_NULL)

        val results = mutableListOf<LaunchResult<Int?, LocalError>>()
        underTest().collect { results.add(it) }

        coVerify { launchRepository.getTotalEntriesCache() }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(LocalError.CACHE_DATA_NULL, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should emit an error when there is a problem accessing cache`() = runTest {
        val error = LocalError.CACHE_ERROR
        coEvery { launchRepository.getTotalEntriesCache() } returns LaunchResult.Error(error)

        val results = mutableListOf<LaunchResult<Int?, LocalError>>()
        underTest().collect { results.add(it) }

        coVerify { launchRepository.getTotalEntriesCache() }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(error, (results.first() as LaunchResult.Error).error)
    }
}