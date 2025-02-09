package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDtoEntityMapper
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
    private lateinit var companyDtoEntityMapper: CompanyDtoEntityMapper

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
            companyDtoEntityMapper = companyDtoEntityMapper
        )
    }

    @Test
    fun `getCompanyApi success returns companyDto and inserts to cache success`() = runTest {
        coEvery { companyNetworkDataSource.getCompanyApi() } returns Result.Success(companyDto)
        every { companyDtoEntityMapper.dtoToEntity(companyDto) } returns companyEntity
        coEvery { companyDiskDataSource.insert(companyEntity) } returns Result.Success(1)

        val result = underTest.getCompanyApi()

        assertTrue(result is Result.Success)
        assertEquals(Unit, (result).data)

        coVerify {
            companyNetworkDataSource.getCompanyApi()
            companyDiskDataSource.insert(companyEntity)
        }
        verify { companyDtoEntityMapper.dtoToEntity(companyDto) }
    }

    @Test
    fun `getCompanyApi success returns error`() = runTest {
        val expected = DataError.NETWORK_UNKNOWN_ERROR
        coEvery { companyNetworkDataSource.getCompanyApi() } returns Result.Error(expected)

        val result = underTest.getCompanyApi()

        assertTrue(result is Result.Error)
        assertEquals(expected, (result).error)

        coVerify {
            companyNetworkDataSource.getCompanyApi()
        }
    }

    @Test
    fun `getCompanyApi success returns companyDto and inserts to cache error`() = runTest {
        val expected = DataError.CACHE_ERROR
        coEvery { companyNetworkDataSource.getCompanyApi() } returns Result.Success(companyDto)
        every { companyDtoEntityMapper.dtoToEntity(companyDto) } returns companyEntity
        coEvery { companyDiskDataSource.insert(companyEntity) } returns Result.Error(expected)

        val result = underTest.getCompanyApi()

        assertTrue(result is Result.Error)
        assertEquals(expected, (result).error)

        coVerify {
            companyNetworkDataSource.getCompanyApi()
            companyDiskDataSource.insert(companyEntity)
        }
        verify { companyDtoEntityMapper.dtoToEntity(companyDto) }
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