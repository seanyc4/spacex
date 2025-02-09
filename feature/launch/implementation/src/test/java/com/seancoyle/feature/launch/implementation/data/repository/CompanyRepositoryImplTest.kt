package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDtoDomainMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyDiskDataSource
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRepositoryImpl
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
    private lateinit var companyDiskDataSource: CompanyDiskDataSource

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
            companyDiskDataSource = companyDiskDataSource,
            companyCacheMapper = companyCacheMapper,
            companyDtoEntityMapper = TODO()
        )
    }

    @Test
    fun `getCompany returns mapped company on success`() = runTest {
        coEvery { companyNetworkDataSource.getCompanyApi() } returns Result.Success(companyDto)
        every { companyDtoDomainMapper.dtoToDomain(companyDto) } returns companyModel

        val result = underTest.getCompanyApi()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyNetworkDataSource.getCompanyApi() }
        verify { companyDtoDomainMapper.dtoToDomain(companyDto) }
    }

    @Test
    fun `insertCompany returns result on success`() = runTest {
        val id = 1L
        every { companyCacheMapper.domainToEntity(companyModel) } returns companyEntity
        coEvery { companyDiskDataSource.insert(companyEntity) } returns Result.Success(id)

        val result = underTest.insertCompany(companyModel)

        assertTrue(result is Result.Success)
        assertEquals(id, (result).data)

        verify { companyCacheMapper.domainToEntity(companyModel) }
        coVerify { companyDiskDataSource.insert(companyEntity) }
    }

    @Test
    fun `getCompanyFromCache returns mapped company on success`() = runTest {
        coEvery { companyDiskDataSource.get() } returns Result.Success(companyEntity)
        every { companyCacheMapper.entityToDomain(companyEntity) } returns companyModel

        val result = underTest.getCompanyCache()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyDiskDataSource.get() }
        verify { companyCacheMapper.entityToDomain(companyEntity) }
    }

    @Test
    fun `deleteAllCompanyCache returns result on success`() = runTest {
        coEvery { companyDiskDataSource.deleteAll() } returns Result.Success(Unit)

        val result = underTest.deleteAllCompanyCache()

        assertTrue(result is Result.Success)

        coVerify { companyDiskDataSource.deleteAll() }
    }

}