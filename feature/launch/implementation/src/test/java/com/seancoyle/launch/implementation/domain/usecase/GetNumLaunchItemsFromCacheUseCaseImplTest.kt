package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
class GetNumLaunchItemsCacheUseCaseImplTest {

    @MockK
    private lateinit var cacheDataSource: LaunchCacheDataSource

    private lateinit var underTest: GetNumLaunchItemsCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetNumLaunchItemsCacheUseCaseImpl(cacheDataSource)
    }

    @Test
    fun `invoke should emit the correct number of entries when data is available`() = runTest {
        val totalEntries = 42
        coEvery { cacheDataSource.getTotalEntries() } returns Result.Success(totalEntries)

        val results = mutableListOf<Result<Int?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(totalEntries, (results.first() as Result.Success).data)
    }

   /* @Test
    fun `invoke should emit null when no entries are found`() = runTest {
        coEvery { cacheDataSource.getTotalEntries() } returns Result.Success(null)

        val results = mutableListOf<Result<Int?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertNull((results.first() as Result.Success).data)
    }*/

    @Test
    fun `invoke should emit an error when there is a problem accessing cache`() = runTest {
        val error = DataError.CACHE_ERROR
        coEvery { cacheDataSource.getTotalEntries() } returns Result.Error(error)

        val results = mutableListOf<Result<Int?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }


    companion object {
        private const val TOTAL_ENTRIES = 10
    }
}