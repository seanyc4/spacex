package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.domain.cache.CompanyCacheDataSource
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
    private lateinit var cacheDataSource: CompanyCacheDataSource

    private lateinit var underTest: InsertCompanyToCacheUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = InsertCompanyToCacheUseCaseImpl(cacheDataSource)
    }

    @Test
    fun `invoke should return success when data source succeeds`() = runTest {
        val expectedResult: Result<Long, DataError> = Result.Success(1L)

        coEvery { cacheDataSource.insert(companyModel) } returns expectedResult

        val result = underTest(companyModel)

        assertTrue(result is Result.Success)
        assertEquals(1L, result.data)
    }

    @Test
    fun `invoke should return error when data source fails`() = runTest {
        val errorResult: Result<Long, DataError> = Result.Error(DataError.CACHE_ERROR)

        coEvery { cacheDataSource.insert(companyModel) } returns errorResult

        val result = underTest(companyModel)

        assertTrue(result is Result.Error)
        assertEquals(DataError.CACHE_ERROR, result.error)
    }
}
