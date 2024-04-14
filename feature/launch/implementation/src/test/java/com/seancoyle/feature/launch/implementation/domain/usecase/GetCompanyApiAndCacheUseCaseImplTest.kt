package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class GetCompanyApiAndCacheUseCaseImplTest {

    @MockK
    private lateinit var insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase

    @MockK
    private lateinit var networkDataSource: CompanyInfoNetworkDataSource

    private lateinit var underTest: GetCompanyApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyApiAndCacheUseCaseImpl(
            insertCompanyInfoToCacheUseCase = insertCompanyInfoToCacheUseCase,
            networkDataSource = networkDataSource
        )
    }

    @Test
    fun `invoke should return company from cache on network success and cache success`() = runTest {
        coEvery { networkDataSource.getCompany() } returns Result.Success(COMPANY_INFO)
        coEvery { insertCompanyInfoToCacheUseCase(COMPANY_INFO) } returns Result.Success(
            COMPANY_INSERT_SUCCESS
        )

        val results = mutableListOf<Result<Company, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(COMPANY_INFO, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should return error when network fails`() = runTest {
        val error = DataError.NETWORK_UNKNOWN_ERROR
        coEvery { networkDataSource.getCompany() } returns Result.Error(error)

        val results = mutableListOf<Result<Company, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should return cache error on network success and cache failure`() = runTest {
        val cacheError = DataError.CACHE_ERROR
        coEvery { networkDataSource.getCompany() } returns Result.Success(COMPANY_INFO)
        coEvery { insertCompanyInfoToCacheUseCase(COMPANY_INFO) } returns Result.Error(cacheError)

        val results = mutableListOf<Result<Company, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(cacheError, (results.first() as Result.Error).error)
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

        const val COMPANY_INSERT_SUCCESS = 1L
    }
}