package com.seancoyle.launch_usecases.launch

import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSource
import com.seancoyle.launch_datasource.network.LaunchNetworkDataSource
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_datasource_test.network.MockWebServerResponseLaunchList.launchList
import com.seancoyle.launch_models.model.launch.LaunchFactory
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_usecases.launch.GetLaunchListFromNetworkAndInsertToCacheUseCase.Companion.LAUNCH_ERROR
import com.seancoyle.launch_usecases.launch.GetLaunchListFromNetworkAndInsertToCacheUseCase.Companion.LAUNCH_INSERT_SUCCESS
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
class GetLaunchItemsFromNetworkInsertToCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // system in test
    private lateinit var getLaunchListFromNetworkAndInsertToCacheUseCase: GetLaunchListFromNetworkAndInsertToCacheUseCase

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
        getLaunchListFromNetworkAndInsertToCacheUseCase = GetLaunchListFromNetworkAndInsertToCacheUseCase(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            cacheDataSource = cacheDataSource,
            launchNetworkDataSource = networkDataSource
        )
    }

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
        assertTrue(cacheDataSource.getAll()?.isEmpty() == true)

        // execute use case
        getLaunchListFromNetworkAndInsertToCacheUseCase(
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
        assertTrue(results?.isNotEmpty() == true)

        // confirm they are actually LaunchModel objects
        assertTrue(results?.get(index = 0) is LaunchModel)

    }

    @Test
    fun getLaunchListFromNetwork_emitHttpError(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        // execute use case
        getLaunchListFromNetworkAndInsertToCacheUseCase(
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