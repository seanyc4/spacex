package com.seancoyle.spacex.business.interactors.company

import com.seancoyle.spacex.business.data.cache.company.FakeCompanyInfoCacheDataSourceImpl
import com.seancoyle.spacex.business.data.cache.company.FakeCompanyInfoDatabase
import com.seancoyle.spacex.business.data.network.company.FakeCompanyInfoNetworkDataSourceImpl
import com.seancoyle.spacex.business.data.network.company.MockWebServerResponseCompanyInfo.companyInfo
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache.Companion.COMPANY_INFO_ERROR
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache.Companion.COMPANY_INFO_INSERT_SUCCESS
import com.seancoyle.spacex.di.DependencyContainer
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

class GetCompanyInfoFromNetworkInsertToCacheTest {

    private val fakeCompanyInfoDatabase = FakeCompanyInfoDatabase()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var getCompanyInfoFromNetworkInsertToCache: GetCompanyInfoFromNetworkAndInsertToCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private lateinit var api: FakeCompanyInfoNetworkDataSourceImpl
    private lateinit var dao: FakeCompanyInfoCacheDataSourceImpl
    private lateinit var infoFactory: CompanyInfoFactory
    private lateinit var networkMapper: CompanyInfoNetworkMapper

    @BeforeEach
    fun setup() {
        dependencyContainer.build()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("v3/launches/")

        networkMapper = CompanyInfoNetworkMapper(
            numberFormatter = dependencyContainer.numberFormatter
        )

        api = FakeCompanyInfoNetworkDataSourceImpl(
            baseUrl = baseUrl,
            networkMapper = networkMapper
        )

        dao = FakeCompanyInfoCacheDataSourceImpl(
            fakeCompanyInfoDatabase = fakeCompanyInfoDatabase
        )

        infoFactory = dependencyContainer.companyInfoFactory

        // instantiate the system in test
        getCompanyInfoFromNetworkInsertToCache = GetCompanyInfoFromNetworkAndInsertToCache(
            cacheDataSource = dao,
            networkDataSource = api,
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
        assert(dao.getCompanyInfo() == null)

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
        val result = dao.getCompanyInfo()

        // confirm the cache is no longer empty
        assert(result != null)

        // confirm the data is a CompanyInfoModel object
        assert(result is CompanyInfoModel)

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
    fun teardown() {
        mockWebServer.shutdown()
    }

}