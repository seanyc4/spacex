package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.contract.data.LaunchCacheDataSource
import com.seancoyle.launch.contract.data.LaunchNetworkDataSource
import com.seancoyle.launch.contract.domain.model.LaunchOptions
import com.seancoyle.launch.contract.domain.model.ViewModel
import com.seancoyle.launch.contract.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.data.network.MockWebServerResponseLaunchList.launchList
import com.seancoyle.launch.implementation.domain.GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.LaunchDependencies
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
class GetLaunchItemsFromNetworkInsertToCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val dependencies: LaunchDependencies = LaunchDependencies()
    private lateinit var cacheDataSource: LaunchCacheDataSource
    private lateinit var networkDataSource: LaunchNetworkDataSource
    private lateinit var mockWebServer: MockWebServer
    private lateinit var launchOptions: LaunchOptions
    private lateinit var underTest: GetLaunchesFromNetworkAndInsertToCacheUseCase

    @BeforeEach
    fun setup() {
        dependencies.build()
        cacheDataSource = dependencies.launchCacheDataSource
        networkDataSource = dependencies.networkDataSource
        mockWebServer = dependencies.mockWebServer
        launchOptions = dependencies.launchOptions

        underTest =
            GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource,
                launchNetworkDataSource = networkDataSource,
                launchOptions = launchOptions
            )
    }

    @Test
    fun getLaunchItemsFromNetwork_InsertToCache_GetFromCache(): Unit = runBlocking {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(launchList)
        )

        cacheDataSource.deleteAll()
        assertTrue(cacheDataSource.getAll()?.isEmpty() == true)

        underTest(
            event = LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvents
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvents.eventName() + EVENT_CACHE_INSERT_SUCCESS
            )
        }

        // get items inserted from network
        val results = cacheDataSource.getAll()

        // results should contain a list of launch items
        assertTrue(results?.isNotEmpty() == true)

        // confirm they are actually LaunchModel objects
        assertTrue(results?.get(index = 0) is ViewModel)

    }

    @Test
    fun getLaunchListFromNetwork_emitHttpError(): Unit = runBlocking {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        underTest(
            event = LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvents
        ).collect { value ->
            assertEquals(
                value?.stateMessage?.response?.message,
                LaunchEvents.GetLaunchesFromNetworkAndInsertToCacheEvents.eventName() + EVENT_NETWORK_ERROR
            )
        }
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

}