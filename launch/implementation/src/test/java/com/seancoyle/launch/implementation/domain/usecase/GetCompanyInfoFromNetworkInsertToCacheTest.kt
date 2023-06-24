package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.MockWebServerResponseCompanyInfo.companyInfo
import com.seancoyle.launch.implementation.domain.CompanyDependencies
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.presentation.LaunchEvents
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
class GetCompanyInfoFromNetworkInsertToCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val dependencies: CompanyDependencies = CompanyDependencies()
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource
    private lateinit var networkDataSource: CompanyInfoNetworkDataSource
    private lateinit var mockWebServer: MockWebServer
    private lateinit var underTest: GetCompanyInfoFromNetworkAndInsertToCacheUseCase

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        networkDataSource = dependencies.companyInfoNetworkSource
        mockWebServer = dependencies.mockWebServer

        underTest =
            GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource,
                networkDataSource = networkDataSource
            )
    }

    @Test
    fun getCompanyInfoFromNetwork_InsertToCache_GetFromCache() = runBlocking {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(companyInfo)
        )

        cacheDataSource.deleteAll()
        assert(cacheDataSource.getCompanyInfo() == null)

        underTest(
            event = LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvents
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvents.eventName() + EVENT_CACHE_INSERT_SUCCESS
            )
        }

        val result = cacheDataSource.getCompanyInfo()
        assertTrue(result != null)
        assertTrue(result is CompanyInfo)
    }

    @Test
    fun getCompanyInfoFromNetwork_emitHttpError() = runBlocking {

         mockWebServer.enqueue(
             MockResponse()
                 .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                 .setBody("{}")
         )

        underTest(
            event = LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvents
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.GetCompanyInfoFromNetworkAndInsertToCacheEvents.eventName() + EVENT_NETWORK_ERROR
            )
        }
    }

    @AfterEach
    internal fun tearDown() {
        mockWebServer.shutdown()
    }
}