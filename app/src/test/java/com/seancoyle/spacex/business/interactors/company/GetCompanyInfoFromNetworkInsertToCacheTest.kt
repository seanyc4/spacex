package com.seancoyle.spacex.business.interactors.company

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.company.MockWebServerResponseCompanyInfo.companyInfo
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache.Companion.COMPANY_INFO_ERROR
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache.Companion.COMPANY_INFO_INSERT_SUCCESS
import com.seancoyle.spacex.di.CompanyDependencies
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

class GetCompanyInfoFromNetworkInsertToCacheTest {

    // system in test
    private lateinit var getCompanyInfoFromNetworkInsertToCache: GetCompanyInfoFromNetworkAndInsertToCache

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
        getCompanyInfoFromNetworkInsertToCache = GetCompanyInfoFromNetworkAndInsertToCache(
            cacheDataSource = cacheDataSource,
            networkDataSource = networkDataSource,
            factory = infoFactory
        )
    }

    /**
     * 1. Is the company info retrieved from the network?
     * 2. Is the company info inserted into the cache?
     * 3. Check the company info is valid CompanyInfoModel?
     */
    @Test
    fun getCompanyInfoFromNetwork_InsertToCache_GetFromCache(): Unit = runBlocking {

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
        getCompanyInfoFromNetworkInsertToCache.execute(
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

    /**
     * Simulate a bad request
     */
    @Test
    fun getCompanyInfoFromNetwork_emitHttpError(): Unit = runBlocking {

        // condition the response
         mockWebServer.enqueue(
             MockResponse()
                 .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                 .setBody("{}")
         )

        // execute use case
        getCompanyInfoFromNetworkInsertToCache.execute(
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