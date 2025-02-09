package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.mapper.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.mapper.CompanyDtoDomainMapper
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
import com.seancoyle.feature.launch.implementation.util.TestData.companyEntity
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompanyRepositoryImplTest {

    @MockK
    private lateinit var companyNetworkDataSource: CompanyNetworkDataSource

    @MockK
    private lateinit var companyCacheDataSource: CompanyCacheDataSource

    @MockK
    private lateinit var companyDtoDomainMapper: CompanyDtoDomainMapper

    @MockK
    private lateinit var companyCacheMapper: CompanyDomainEntityMapper

    private lateinit var underTest: CompanyRepository


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyRepositoryImpl(
            companyNetworkDataSource = companyNetworkDataSource,
            companyCacheDataSource = companyCacheDataSource,
            companyDtoDomainMapper = companyDtoDomainMapper,
            companyCacheMapper = companyCacheMapper
        )
    }

    @Test
    fun `getCompany returns mapped company on success`() = runTest {
        coEvery { companyNetworkDataSource.getCompany() } returns Result.Success(companyDto)
        every { companyDtoDomainMapper.dtoToDomain(companyDto) } returns companyModel

        val result = underTest.getCompany()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyNetworkDataSource.getCompany() }
        verify { companyDtoDomainMapper.dtoToDomain(companyDto) }
    }

    @Test
    fun `insertCompany returns result on success`() = runTest {
        val id = 1L
        every { companyCacheMapper.domainToEntity(companyModel) } returns companyEntity
        coEvery { companyCacheDataSource.insert(companyEntity) } returns Result.Success(id)

        val result = underTest.insertCompany(companyModel)

        assertTrue(result is Result.Success)
        assertEquals(id, (result).data)

        verify { companyCacheMapper.domainToEntity(companyModel) }
        coVerify { companyCacheDataSource.insert(companyEntity) }
    }

    @Test
    fun `getCompanyFromCache returns mapped company on success`() = runTest {
        coEvery { companyCacheDataSource.getCompany() } returns Result.Success(companyEntity)
        every { companyCacheMapper.entityToDomain(companyEntity) } returns companyModel

        val result = underTest.getCompanyFromCache()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyCacheDataSource.getCompany() }
        verify { companyCacheMapper.entityToDomain(companyEntity) }
    }

    @Test
    fun `deleteAllCompanyCache returns result on success`() = runTest {
        coEvery { companyCacheDataSource.deleteAll() } returns Result.Success(Unit)

        val result = underTest.deleteAllCompanyCache()

        assertTrue(result is Result.Success)

        coVerify { companyCacheDataSource.deleteAll() }
    }

}