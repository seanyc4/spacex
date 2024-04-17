package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.test.TestCoroutineRule
import com.seancoyle.feature.launch.implementation.util.TestData.companyDto
import com.seancoyle.feature.launch.implementation.util.TestData.companyModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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

    @MockK
    private lateinit var networkMapper: CompanyNetworkMapper

    @MockK(relaxed = true)
    private lateinit var crashlytics: Crashlytics

    private lateinit var underTest: CompanyNetworkDataSourceImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        underTest = CompanyNetworkDataSourceImpl(
            api = api,
            networkMapper = networkMapper,
            crashlytics = crashlytics,
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `getCompany returns mapped company when API call is successful`() = runTest {
        coEvery { api.getCompany() } returns companyDto
        coEvery { networkMapper.mapFromEntity(companyDto) } returns companyModel

        val result = underTest.getCompany()

        assertTrue(result is Result.Success)
        assertEquals(companyModel, result.data)
    }

    @Test
    fun `getCompany returns DataError when API call fails`() = runTest {
        val exception = RuntimeException("Network Failure")
        coEvery { api.getCompany() } throws exception

        val result = underTest.getCompany()

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_UNKNOWN_ERROR, result.error)
    }
}