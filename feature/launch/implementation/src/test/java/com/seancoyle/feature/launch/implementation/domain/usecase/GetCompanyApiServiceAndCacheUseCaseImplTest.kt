package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.util.TestData.COMPANY_INSERT_SUCCESS
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCompanyApiServiceAndCacheUseCaseImplTest {

    @MockK
    private lateinit var insertCompanyToCacheUseCase: InsertCompanyToCacheUseCase

    @MockK
    private lateinit var companyRepository: CompanyRepository

    private lateinit var underTest: GetCompanyApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyApiAndCacheUseCaseImpl(
            insertCompanyToCacheUseCase = insertCompanyToCacheUseCase,
            companyRepository = companyRepository
        )
    }

    @Test
    fun `invoke should return company from cache on network success and cache success`() = runTest {
        coEvery { companyRepository.getCompanyApi() } returns Result.Success(companyModel)
        coEvery { insertCompanyToCacheUseCase(companyModel) } returns Result.Success(COMPANY_INSERT_SUCCESS)

        val results = mutableListOf<Result<Company, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(companyModel, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should return error when network fails`() = runTest {
        val error = DataError.NETWORK_UNKNOWN_ERROR
        coEvery { companyRepository.getCompanyApi() } returns Result.Error(error)

        val results = mutableListOf<Result<Company, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should return cache error on network success and cache failure`() = runTest {
        val cacheError = DataError.CACHE_ERROR
        coEvery { companyRepository.getCompanyApi() } returns Result.Success(companyModel)
        coEvery { insertCompanyToCacheUseCase(companyModel) } returns Result.Error(cacheError)

        val results = mutableListOf<Result<Company, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(cacheError, (results.first() as Result.Error).error)
    }
}