package com.seancoyle.feature.launch.implementation.domain.usecase.company

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCompanyCacheUseCaseImplTest {

    @MockK
    private lateinit var companyRepository: CompanyRepository

    private lateinit var underTest: GetCompanyCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyCacheUseCaseImpl(companyRepository)
    }

    @Test
    fun `invoke should return company from cache on success`() = runTest {
        coEvery { companyRepository.getCompanyCache() } returns LaunchResult.Success(companyModel)

        val results = mutableListOf<LaunchResult<Company?, LocalError>>()
        underTest().collect { results.add(it) }

        coVerify { companyRepository.getCompanyCache() }

        assertTrue(results.first() is LaunchResult.Success)
        assertEquals(companyModel, (results.first() as LaunchResult.Success).data)
    }

    @Test
    fun `invoke should return error when cache is empty`() = runTest {
        val error = LocalError.CACHE_ERROR_NO_RESULTS
        coEvery { companyRepository.getCompanyCache() } returns LaunchResult.Error(error)


        val results = mutableListOf<LaunchResult<Company?, LocalError>>()
        underTest().collect { results.add(it) }

        coVerify { companyRepository.getCompanyCache() }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(error, (results.first() as LaunchResult.Error).error)
    }

    @Test
    fun `invoke should return error when cache access fails`() = runTest {
        val error = LocalError.CACHE_ERROR
        coEvery { companyRepository.getCompanyCache() } returns LaunchResult.Error(error)

        val results = mutableListOf<LaunchResult<Company?, LocalError>>()
        underTest().collect { results.add(it) }

        coVerify { companyRepository.getCompanyCache() }

        assertTrue(results.first() is LaunchResult.Error)
        assertEquals(error, (results.first() as LaunchResult.Error).error)
    }
}