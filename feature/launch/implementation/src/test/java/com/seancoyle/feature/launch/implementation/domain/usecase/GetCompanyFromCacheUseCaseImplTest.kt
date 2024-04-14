package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.cache.CompanyCacheDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetCompanyCacheUseCaseImplTest {

    @MockK
    private lateinit var cacheDataSource: CompanyCacheDataSource

    private lateinit var underTest: GetCompanyCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyCacheUseCaseImpl(cacheDataSource)
    }

    @Test
    fun `invoke should return company from cache on success`() = runTest {
        coEvery { cacheDataSource.getCompany() } returns Result.Success(COMPANY_INFO)

        val results = mutableListOf<Result<Company?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(COMPANY_INFO, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should return null when cache is empty`() = runTest {
        coEvery { cacheDataSource.getCompany() } returns Result.Success(null)

        val results = mutableListOf<Result<Company?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertNull((results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should return error when cache access fails`() = runTest {
        val error = DataError.CACHE_ERROR
        coEvery { cacheDataSource.getCompany() } returns Result.Error(error)

        val results = mutableListOf<Result<Company?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }

    private companion object {
        val COMPANY_INFO = Company(
            id = "1",
            employees = "employees",
            founded = 2000,
            founder = "founder",
            launchSites = 4,
            name = "name",
            valuation = "valuation"
        )
    }
}