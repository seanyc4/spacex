package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import com.seancoyle.feature.launch.implementation.util.TestData.launchesModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InsertLaunchesToCacheUseCaseImplTest {

    @MockK
    private lateinit var launchPreferencesRepository: LaunchPreferencesRepository

    private lateinit var underTest: InsertLaunchesToCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = InsertLaunchesToCacheUseCaseImpl(launchPreferencesRepository)
    }

    @Test
    fun `invoke should return success when data source succeeds`() = runTest {
        val expectedResult = longArrayOf(1L)
        val insertResult: Result<LongArray, DataError> = Result.Success(expectedResult)

        coEvery { launchPreferencesRepository.insertLaunches(launchesModel) } returns insertResult

        val result = underTest(launchesModel)

        assertTrue(result is Result.Success)
        assertEquals(expectedResult, result.data)
    }

    @Test
    fun `invoke should return error when data source fails`() = runTest {
        val errorResult: Result<LongArray, DataError> = Result.Error(DataError.CACHE_ERROR)

        coEvery { launchPreferencesRepository.insertLaunches(launchesModel) } returns errorResult

        val result = underTest(launchesModel)

        assertTrue(result is Result.Error)
        assertEquals(DataError.CACHE_ERROR, result.error)
    }
}