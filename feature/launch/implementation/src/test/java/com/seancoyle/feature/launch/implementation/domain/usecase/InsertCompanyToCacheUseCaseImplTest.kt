package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InsertCompanyToCacheUseCaseImplTest {

    @MockK
    private lateinit var spaceXRepository: SpaceXRepository

    private lateinit var underTest: InsertCompanyToCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = InsertCompanyToCacheUseCaseImpl(spaceXRepository)
    }

    @Test
    fun `invoke should return success when data source succeeds`() = runTest {
        val expectedResult: Result<Long, DataError> = Result.Success(1L)

        coEvery { spaceXRepository.insertCompany(companyModel) } returns expectedResult

        val result = underTest(companyModel)

        assertTrue(result is Result.Success)
        assertEquals(1L, result.data)
    }

    @Test
    fun `invoke should return error when data source fails`() = runTest {
        val errorResult: Result<Long, DataError> = Result.Error(DataError.CACHE_ERROR)

        coEvery { spaceXRepository.insertCompany(companyModel) } returns errorResult

        val result = underTest(companyModel)

        assertTrue(result is Result.Error)
        assertEquals(DataError.CACHE_ERROR, result.error)
    }
}
