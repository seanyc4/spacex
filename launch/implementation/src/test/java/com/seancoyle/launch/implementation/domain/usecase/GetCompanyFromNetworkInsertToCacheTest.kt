package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.data.CompanyCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.api.presentation.LaunchState
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.network.MockWebServerResponseCompanyInfo.companyInfo
import com.seancoyle.launch.implementation.presentation.LaunchEvents
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class GetCompanyFromNetworkInsertToCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: CompanyCacheDataSource

    @MockK
    private lateinit var networkDataSource: CompanyInfoNetworkDataSource

    private lateinit var mockWebServer: MockWebServer

    private lateinit var underTest: GetCompanyInfoFromNetworkAndInsertToCacheUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mockWebServer = MockWebServer()
        underTest =
            GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource,
                networkDataSource = networkDataSource
            )
    }

    @AfterEach
    fun cleanup() {
        mockWebServer.shutdown()
    }

    @Test
    fun whenGetCompanyInfoFromNetwork_thenItemsAreInsertedIntoCache(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(companyInfo))
        coEvery { networkDataSource.getCompanyInfo() } returns COMPANY_INFO
        coEvery { cacheDataSource.insert(COMPANY_INFO) } returns 1
        var apiResult: ApiResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetCompanyInfoApiAndCacheEvent).collect { value ->
            apiResult = value
        }

        assertEquals(
            apiResult?.stateMessage?.response?.message,
            LaunchEvents.GetCompanyInfoApiAndCacheEvent.eventName() + EVENT_CACHE_INSERT_SUCCESS
        )
    }

    @Test
    fun whenGetCompanyInfoFromNetwork_thenItemsCanBeRetrievedFromCache(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(companyInfo))
        coEvery { cacheDataSource.insert(COMPANY_INFO) } returns 1
        coEvery { cacheDataSource.getCompany() } returns COMPANY_INFO


        underTest(event = LaunchEvents.GetCompanyInfoApiAndCacheEvent)

        val resultFromCache = cacheDataSource.getCompany()

        assertTrue(resultFromCache != null)
        assertTrue(resultFromCache is Company)
    }

    @Test
    fun whenGetCompanyInfoFromNetwork_andNetworkErrorOccurs_thenErrorEventIsEmitted(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).setBody("{}"))
        var apiResult: ApiResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetCompanyInfoApiAndCacheEvent).collect { value ->
            apiResult = value
        }

        assertEquals(
            apiResult?.stateMessage?.response?.message,
            LaunchEvents.GetCompanyInfoApiAndCacheEvent.eventName() + EVENT_NETWORK_ERROR
        )
    }

    companion object {
        private val COMPANY_INFO = Company(
            id = "1",
            employees = "employees",
            founded = 2000,
            founder = "founder",
            launchSites = 4,
            name = "name",
            valuation = "valuation"
        )
    }
}