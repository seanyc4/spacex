package com.seancoyle.feature.launch.implementation.domain.usecase.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCompanyApiServiceAndCacheUseCaseImplTest {

    @MockK
    private lateinit var companyRepository: CompanyRepository

    private lateinit var underTest: GetCompanyApiAndCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyApiAndCacheUseCaseImpl(companyRepository)
    }

    @Test
    fun `invoke should return company from API on success `() = runTest {
        coEvery { companyRepository.getCompanyApi() } returns Result.Success(Unit)

        val results = mutableListOf<Result<Unit, DataError>>()
        underTest().collect { results.add(it) }

        coVerify { companyRepository.getCompanyApi() }

        assertTrue(results.first() is Result.Success)
        assertEquals(Unit, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should return error when network fails`() = runTest {
        val error = DataError.NETWORK_UNKNOWN_ERROR
        coEvery { companyRepository.getCompanyApi() } returns Result.Error(error)

        val results = mutableListOf<Result<Unit, DataError>>()
        underTest().collect { results.add(it) }

        coVerify { companyRepository.getCompanyApi() }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }
}