package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core_testing.MainCoroutineRule
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.presentation.LaunchState
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource
import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.network.MockWebServerResponseCompanyInfo.companyInfo
import com.seancoyle.launch.implementation.presentation.state.LaunchEvents
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
class GetCompanyApiAndCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: CompanyCacheDataSource

    @MockK
    private lateinit var networkDataSource: CompanyInfoNetworkDataSource

    private lateinit var mockWebServer: MockWebServer

    private lateinit var underTest: GetCompanyApiAndCacheUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mockWebServer = MockWebServer()
        underTest =
            GetCompanyApiAndCacheUseCaseImpl(
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
        coEvery { networkDataSource.getCompany() } returns COMPANY_INFO
        coEvery { cacheDataSource.insert(COMPANY_INFO) } returns 1
        var dataResult: com.seancoyle.core.domain.DataResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetCompanyApiAndCacheEvent).collect { value ->
            dataResult = value
        }

        assertEquals(
            dataResult?.stateMessage?.response?.message,
            LaunchEvents.GetCompanyApiAndCacheEvent.eventName() + EVENT_CACHE_INSERT_SUCCESS
        )
    }

    @Test
    fun whenGetCompanyInfoFromNetwork_thenItemsCanBeRetrievedFromCache(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(companyInfo))
        coEvery { cacheDataSource.insert(COMPANY_INFO) } returns 1
        coEvery { cacheDataSource.getCompany() } returns COMPANY_INFO


        underTest(event = LaunchEvents.GetCompanyApiAndCacheEvent)

        val resultFromCache = cacheDataSource.getCompany()

        assertTrue(resultFromCache != null)
        assertTrue(resultFromCache is Company)
    }

    @Test
    fun whenGetCompanyInfoFromNetwork_andNetworkErrorOccurs_thenErrorEventIsEmitted(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).setBody("{}"))
        var dataResult: com.seancoyle.core.domain.DataResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetCompanyApiAndCacheEvent).collect { value ->
            dataResult = value
        }

        assertEquals(
            dataResult?.stateMessage?.response?.message,
            LaunchEvents.GetCompanyApiAndCacheEvent.eventName() + EVENT_NETWORK_ERROR
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