package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.mapper.CompanyMapper
import com.seancoyle.feature.launch.implementation.data.mapper.RemoteErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
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

class CompanyRemoteDataSourceImplTest {

    @MockK
    private lateinit var api: CompanyApi

    @MockK
    private lateinit var companyMapper: CompanyMapper

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    @RelaxedMockK
    private lateinit var remoteErrorMapper: RemoteErrorMapper

    private lateinit var underTest: CompanyRemoteDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyRemoteDataSourceImpl(
            api = api,
            crashlytics = crashlytics,
            errorMapper = remoteErrorMapper,
            mapper = companyMapper
        )
    }

    @Test
    fun `getCompany returns company DTO when API call is successful`() = runTest {
        coEvery { api.getCompany() } returns companyDto
        every { companyMapper.dtoToDomain(companyDto) } returns companyModel

        val result = underTest.getCompanyApi()

        coVerify { api.getCompany() }
        verify { companyMapper.dtoToDomain(companyDto) }

        assertTrue(result is LaunchResult.Success)
        assertEquals(companyModel, result.data)
    }

    @Test
    fun `getCompany returns DataError when API call fails`() = runTest {
        val expected = RemoteError.NETWORK_UNKNOWN_ERROR
        val throwable = Throwable()

        coEvery { api.getCompany() } throws throwable
        every { remoteErrorMapper.map(throwable) } returns expected
        every { companyMapper.dtoToDomain(companyDto) } returns companyModel

        val result = underTest.getCompanyApi()

        coVerify { api.getCompany() }
        verify { crashlytics.logException(throwable) }

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)
    }
}