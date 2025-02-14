package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CompanyNetworkDataSourceImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var api: CompanyApiService

    @RelaxedMockK
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: CompanyNetworkDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyNetworkDataSourceImpl(
            api = api,
            crashlytics = crashlytics,
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher
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
        val exception = RuntimeException("Network Failure")
        coEvery { api.getCompany() } throws exception

        val result = underTest.getCompanyApi()

        coVerify { api.getCompany() }

        assertTrue(result is LaunchResult.Error)
        assertEquals(DataSourceError.NETWORK_UNKNOWN_ERROR, result.error)
    }
}