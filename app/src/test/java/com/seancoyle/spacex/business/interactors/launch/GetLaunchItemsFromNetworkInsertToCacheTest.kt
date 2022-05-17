package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.data.network.launch.MockWebServerResponseLaunchList.launchList
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.GetLaunchListFromNetworkAndInsertToCache.Companion.LAUNCH_ERROR
import com.seancoyle.spacex.business.interactors.launch.GetLaunchListFromNetworkAndInsertToCache.Companion.LAUNCH_INSERT_SUCCESS
import com.seancoyle.spacex.di.LaunchDependencies
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

class GetLaunchItemsFromNetworkInsertToCacheTest {

    // system in test
    private lateinit var getLaunchListFromNetworkAndInsertToCache: GetLaunchListFromNetworkAndInsertToCache

    // dependencies
    private val dependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var networkDataSource: LaunchNetworkDataSource
    private lateinit var factory: LaunchFactory
    private lateinit var mockWebServer: MockWebServer

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.launchCacheDataSource
        networkDataSource = dependencies.networkDataSource
        factory = dependencies.launchFactory
        mockWebServer = dependencies.mockWebServer

        // instantiate the system in test
        getLaunchListFromNetworkAndInsertToCache = GetLaunchListFromNetworkAndInsertToCache(
            cacheDataSource = cacheDataSource,
            launchNetworkDataSource = networkDataSource,
            factory = factory
        )
    }

    /**
     * 1. Are the launch retrieved from the network?
     * 2. Are the launch inserted into the cache?
     * 3. Check the list of launch items are valid LaunchModel?
     */
    @Test
    fun getLaunchItemsFromNetwork_InsertToCache_GetFromCache(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(launchList)
        )

        // confirm the cache is empty to start
        cacheDataSource.deleteAll()
        assert(cacheDataSource.getAll()?.isEmpty() == true)

        // execute use case
        getLaunchListFromNetworkAndInsertToCache.execute(
            launchOptions = dependencies.launchOptions,
            stateEvent = LaunchStateEvent.GetLaunchItemsFromNetworkAndInsertToCacheEvent(
                launchOptions = dependencies.launchOptions
            )
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LAUNCH_INSERT_SUCCESS
            )
        }

        // get items inserted from network
        val results = cacheDataSource.getAll()

        // results should contain a list of launch items
        assert(results?.isNotEmpty() == true)

        // confirm they are actually LaunchModel objects
        assert(results?.get(index = 0) is LaunchModel)

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
            launchOptions = dependencies.launchOptions,
            stateEvent = LaunchStateEvent.GetLaunchItemsFromNetworkAndInsertToCacheEvent(
                launchOptions = dependencies.launchOptions
            )
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