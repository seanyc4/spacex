package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.cache.company.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteErrorMapper
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
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompanyRepositoryImplTest {

    @MockK
    private lateinit var companyRemoteDataSource: CompanyRemoteDataSource

    @MockK
    private lateinit var companyLocalDataSource: CompanyLocalDataSource

    @MockK
    private lateinit var companyDtoEntityMapper: CompanyDtoEntityMapper

    @MockK
    private lateinit var companyCacheMapper: CompanyDomainEntityMapper

    @RelaxedMockK
    private lateinit var remoteErrorMapper: RemoteErrorMapper

    @RelaxedMockK
    private lateinit var localErrorMapper: LocalErrorMapper

    private lateinit var underTest: CompanyRepository


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyRepositoryImpl(
            companyRemoteDataSource = companyRemoteDataSource,
            companyLocalDataSource = companyLocalDataSource,
            companyCacheMapper = companyCacheMapper,
            companyDtoEntityMapper = companyDtoEntityMapper,
            remoteErrorMapper = remoteErrorMapper,
            localErrorMapper = localErrorMapper
        )
    }

    @Test
    fun `getCompanyApi success returns companyDto and inserts to cache success`() = runTest {
        coEvery { companyRemoteDataSource.getCompanyApi() } returns Result.success(companyDto)
        every { companyDtoEntityMapper.dtoToEntity(companyDto) } returns companyEntity
        coEvery { companyLocalDataSource.insert(companyEntity) } returns Result.success(1)

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Success)
        assertEquals(Unit, (result).data)

        coVerify {
            companyRemoteDataSource.getCompanyApi()
            companyLocalDataSource.insert(companyEntity)
        }
        verify { companyDtoEntityMapper.dtoToEntity(companyDto) }
    }

    @Test
    fun `getCompanyApi success returns error`() = runTest {
        val expected = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { companyRemoteDataSource.getCompanyApi() } returns Result.failure(Throwable())

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, (result).error)

        coVerify {
            companyRemoteDataSource.getCompanyApi()
        }
    }

    @Test
    fun `getCompanyApi success returns companyDto and inserts to cache error`() = runTest {
        val expected = LocalError.CACHE_ERROR
        val throwable = Throwable()
        coEvery { companyRemoteDataSource.getCompanyApi() } returns Result.success(companyDto)
        every { companyDtoEntityMapper.dtoToEntity(companyDto) } returns companyEntity
        coEvery { companyLocalDataSource.insert(companyEntity) } returns Result.failure(throwable)

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, (result).error)

        coVerify {
            companyRemoteDataSource.getCompanyApi()
            companyLocalDataSource.insert(companyEntity)
        }
        verify { companyDtoEntityMapper.dtoToEntity(companyDto) }
    }

    @Test
    fun `getCompanyFromCache returns mapped company on success`() = runTest {
        coEvery { companyLocalDataSource.get() } returns Result.success(companyEntity)
        every { companyCacheMapper.entityToDomain(companyEntity) } returns companyModel

        val result = underTest.getCompanyCache()

        assertTrue(result is LaunchResult.Success)
        assertEquals(companyModel, (result).data)

        coVerify { companyLocalDataSource.get() }
        verify { companyCacheMapper.entityToDomain(companyEntity) }
    }

    @Test
    fun `deleteAllCompanyCache returns result on success`() = runTest {
        coEvery { companyLocalDataSource.deleteAll() } returns Result.success(Unit)

        val result = underTest.deleteAllCompanyCache()

        assertTrue(result is LaunchResult.Success)

        coVerify { companyLocalDataSource.deleteAll() }
    }

}