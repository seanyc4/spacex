package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.launch.FakeLaunchDatabase
import com.seancoyle.spacex.business.data.cache.launch.FakeLaunchCacheDataSourceImpl
import com.seancoyle.spacex.business.data.network.launch.FakeLaunchNetworkDataSourceImpl
import com.seancoyle.spacex.business.data.network.launch.MockWebServerResponseLaunchList.launchList
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.interactors.launch.GetLaunchItemsFromNetworkAndInsertToCache.Companion.LAUNCH_ERROR
import com.seancoyle.spacex.business.interactors.launch.GetLaunchItemsFromNetworkAndInsertToCache.Companion.LAUNCH_INSERT_SUCCESS
import com.seancoyle.spacex.di.DependencyContainer
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
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

class GetLaunchItemsFromNetworkInsertToCacheTest {

    private val fakeLaunchListDatabase = FakeLaunchDatabase()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var getLaunchListFromNetworkAndInsertToCache: GetLaunchItemsFromNetworkAndInsertToCache

    // dependencies
    private val dependencyContainer: DependencyContainer = DependencyContainer()
    private lateinit var api: FakeLaunchNetworkDataSourceImpl
    private lateinit var dao: FakeLaunchCacheDataSourceImpl
    private lateinit var factory: LaunchFactory
    private lateinit var networkMapper: LaunchNetworkMapper

    @BeforeEach
    fun setup() {
        dependencyContainer.build()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("v3/launches/")

        networkMapper = LaunchNetworkMapper(
            dateFormatter = dependencyContainer.dateFormatter,
            dateTransformer = dependencyContainer.dateTransformer
        )

        api = FakeLaunchNetworkDataSourceImpl(
            baseUrl = baseUrl,
            networkMapper = networkMapper
        )

        dao = FakeLaunchCacheDataSourceImpl(
            fakeLaunchDatabase = fakeLaunchListDatabase
        )

        factory = dependencyContainer.launchFactory

        // instantiate the system in test
        getLaunchListFromNetworkAndInsertToCache = GetLaunchItemsFromNetworkAndInsertToCache(
            cacheDataSource = dao,
            launchNetworkDataSource = api,
            factory = factory
        )
    }

    /**
     * 1. Are the launch retrieved from the network?
     * 2. Are the launch inserted into the cache?
     * 3. Check the list of launch items are valid LaunchDomainEntity?
     */
    @Test
    fun getLaunchItemsFromNetwork_InsertToCache_GetFromCache(): Unit = runBlocking {
        var launchResult: List<LaunchDomainEntity>? = null

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(launchList)
        )

        // confirm the cache is empty to start
        assert(dao.getAll().isEmpty())

        // execute use case
        getLaunchListFromNetworkAndInsertToCache.execute(
            stateEvent = LaunchStateEvent.GetLaunchListFromNetworkAndInsertToCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LAUNCH_INSERT_SUCCESS
            ).also {
                launchResult = value?.data?.launchList
            }
        }

        // confirm the cache is no longer empty
        assert(dao.getAll().isNotEmpty())

        // Launch Result should contain a list of launch items
        assert(launchResult?.size ?: 0 > 0)

        // confirm they are actually LaunchDomainEntity objects
        assert(launchResult?.get(index = 0) is LaunchDomainEntity)

    }

    /**
     * Simulate a bad request
     */
    @Test
    fun getLaunchListFromNetwork_emitHttpError(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        // execute use case
        getLaunchListFromNetworkAndInsertToCache.execute(
            stateEvent = LaunchStateEvent.GetLaunchListFromNetworkAndInsertToCacheEvent
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LAUNCH_ERROR
            )
        }
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

}