package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
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

class GetCompanyCacheUseCaseImplTest {

    @MockK
    private lateinit var spaceXRepository: SpaceXRepository

    private lateinit var underTest: GetCompanyCacheUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = GetCompanyCacheUseCaseImpl(spaceXRepository)
    }

    @Test
    fun `invoke should return company from cache on success`() = runTest {
        coEvery { spaceXRepository.getCompany() } returns Result.Success(companyModel)

        val results = mutableListOf<Result<Company?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Success)
        assertEquals(companyModel, (results.first() as Result.Success).data)
    }

    @Test
    fun `invoke should return error when cache is empty`() = runTest {
        val error = DataError.CACHE_ERROR_NO_RESULTS
        coEvery { spaceXRepository.getCompany() } returns Result.Error(error)


        val results = mutableListOf<Result<Company?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }

    @Test
    fun `invoke should return error when cache access fails`() = runTest {
        val error = DataError.CACHE_ERROR
        coEvery { spaceXRepository.getCompany() } returns Result.Error(error)

        val results = mutableListOf<Result<Company?, DataError>>()
        underTest().collect { results.add(it) }

        assertTrue(results.first() is Result.Error)
        assertEquals(error, (results.first() as Result.Error).error)
    }
}