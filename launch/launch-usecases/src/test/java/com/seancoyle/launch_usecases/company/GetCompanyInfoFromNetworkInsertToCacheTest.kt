package com.seancoyle.launch_usecases.company

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch_datasource.cache.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch_datasource_test.CompanyDependencies
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_models.model.company.CompanyInfoModel
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromNetworkAndInsertToCacheUseCase.Companion.COMPANY_INFO_ERROR
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromNetworkAndInsertToCacheUseCase.Companion.COMPANY_INFO_INSERT_SUCCESS
import com.seancoyle.launch_usecases.company.MockWebServerResponseCompanyInfo.companyInfo
import com.seancoyle.launch_viewstate.LaunchStateEvent
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

    // system in test
    private lateinit var getCompanyInfoFromNetworkInsertToCache: GetCompanyInfoFromNetworkAndInsertToCacheUseCase

    // dependencies
    private val dependencies: CompanyDependencies = CompanyDependencies()
    private lateinit var cacheDataSource: CompanyInfoCacheDataSource
    private lateinit var networkDataSource: CompanyInfoNetworkDataSource
    private lateinit var infoFactory: CompanyInfoFactory
    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.companyInfoCacheDataSource
        networkDataSource = dependencies.companyInfoNetworkSource
        infoFactory = dependencies.companyInfoFactory
        mockWebServer = dependencies.mockWebServer

        // instantiate the system in test
        getCompanyInfoFromNetworkInsertToCache = GetCompanyInfoFromNetworkAndInsertToCacheUseCase(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource,
            networkDataSource = networkDataSource,
            factory = infoFactory
        )
    }

    @Test
    fun getCompanyInfoFromNetwork_InsertToCache_GetFromCache() = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(companyInfo)
        )


        // confirm the cache is empty to start
        cacheDataSource.deleteAll()
        assert(cacheDataSource.getCompanyInfo() == null)

        // execute use case
        getCompanyInfoFromNetworkInsertToCache(
            stateEvent = LaunchStateEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                COMPANY_INFO_INSERT_SUCCESS
            )
        }

        // get items inserted from network
        val result = cacheDataSource.getCompanyInfo()

        // confirm the cache is no longer empty
        assertTrue(result != null)

        // confirm the data is a CompanyInfoModel object
        assertTrue(result is CompanyInfoModel)

    }

    @Test
    fun getCompanyInfoFromNetwork_emitHttpError() = runBlocking {

        // condition the response
         mockWebServer.enqueue(
             MockResponse()
                 .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                 .setBody("{}")
         )

        // execute use case
        getCompanyInfoFromNetworkInsertToCache(
            stateEvent = LaunchStateEvent.GetCompanyInfoFromNetworkAndInsertToCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                COMPANY_INFO_ERROR
            )
        }
    }


    @AfterEach
    internal fun tearDown() {
        mockWebServer.shutdown()
    }
}