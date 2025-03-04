package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.mapper.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.mapper.CompanyMapper
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

    private lateinit var underTest: CompanyRepository


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyRepositoryImpl(
            companyRemoteDataSource = companyRemoteDataSource,
            companyLocalDataSource = companyLocalDataSource
        )
    }

    @Test
    fun `getCompanyApi success returns companyDto and inserts to cache success`() = runTest {
        coEvery { companyRemoteDataSource.getCompanyApi() } returns LaunchResult.Success(companyModel)
        coEvery { companyLocalDataSource.insert(companyModel) } returns LaunchResult.Success(1)

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Success)
        assertEquals(Unit, (result).data)

        coVerify {
            companyRemoteDataSource.getCompanyApi()
            companyLocalDataSource.insert(companyModel)
        }
    }

    @Test
    fun `getCompanyApi success returns error`() = runTest {
        val expected = RemoteError.NETWORK_UNKNOWN_ERROR
        coEvery { companyRemoteDataSource.getCompanyApi() } returns LaunchResult.Error(expected)

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)

        coVerify { companyRemoteDataSource.getCompanyApi() }
    }

    @Test
    fun `getCompanyApi success returns companyDto and inserts to cache error`() = runTest {
        val expected = LocalError.CACHE_ERROR
        coEvery { companyRemoteDataSource.getCompanyApi() } returns LaunchResult.Success(companyModel)
        coEvery { companyLocalDataSource.insert(companyModel) } returns LaunchResult.Error(expected)

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)

        coVerify {
            companyRemoteDataSource.getCompanyApi()
            companyLocalDataSource.insert(companyModel)
        }
    }

    @Test
    fun `getCompanyFromCache returns mapped company on success`() = runTest {
        coEvery { companyLocalDataSource.get() } returns LaunchResult.Success(companyModel)

        val result = underTest.getCompanyCache()

        assertTrue(result is LaunchResult.Success)
        assertEquals(companyModel, result.data)

        coVerify { companyLocalDataSource.get() }
    }

    @Test
    fun `deleteAllCompanyCache returns result on success`() = runTest {
        coEvery { companyLocalDataSource.deleteAll() } returns LaunchResult.Success(Unit)

        val result = underTest.deleteAllCompanyCache()

        assertTrue(result is LaunchResult.Success)

        coVerify { companyLocalDataSource.deleteAll() }
    }

}