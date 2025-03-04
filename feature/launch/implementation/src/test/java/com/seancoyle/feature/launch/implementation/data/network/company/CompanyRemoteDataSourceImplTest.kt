package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CompanyRemoteDataSourceImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var api: CompanyApi

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
            remoteErrorMapper = remoteErrorMapper
        )
    }

    @Test
    fun `getCompany returns company DTO when API call is successful`() = runTest {
        coEvery { api.getCompany() } returns companyDto

        val result = underTest.getCompanyApi()

        coVerify { api.getCompany() }

        assertTrue(result is LaunchResult.Success)
        assertEquals(companyDto, result.data)
    }

    @Test
    fun `getCompany returns DataError when API call fails`() = runTest {
        val expected = RemoteError.NETWORK_UNKNOWN_ERROR
        val throwable = Throwable()

        coEvery { api.getCompany() } throws throwable
        every { remoteErrorMapper.map(throwable) } returns expected


        val result = underTest.getCompanyApi()

        coVerify { api.getCompany() }
        verify { crashlytics.logException(throwable) }

        assertTrue(result is LaunchResult.Error)
        assertEquals(expected, result.error)
    }
}