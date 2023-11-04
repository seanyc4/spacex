package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.core.domain.UsecaseResponses.EVENT_CACHE_INSERT_SUCCESS
import com.seancoyle.core.domain.UsecaseResponses.EVENT_NETWORK_ERROR
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.domain.GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.network.MockWebServerResponseLaunchList.launchList
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
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class GetLaunchItemsFromNetworkInsertToCacheTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: LaunchCacheDataSource

    @MockK
    private lateinit var networkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchOptions: LaunchOptions

    private lateinit var mockWebServer: MockWebServer

    private lateinit var underTest: GetLaunchesFromNetworkAndInsertToCacheUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mockWebServer = MockWebServer()
        underTest =
            GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl(
                ioDispatcher = mainCoroutineRule.testDispatcher,
                cacheDataSource = cacheDataSource,
                launchNetworkDataSource = networkDataSource,
                launchOptions = launchOptions
            )
    }

    @AfterEach
    fun cleanup() {
        mockWebServer.shutdown()
    }

    @Test
    fun whenGetLaunchItemsFromNetwork_thenItemsAreInsertedIntoCache(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(launchList))
        coEvery { networkDataSource.getLaunchList(any()) } returns LAUNCH_LIST
        coEvery { cacheDataSource.insertList(LAUNCH_LIST) } returns longArrayOf(1)

        var apiResult: ApiResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetLaunchesApiAndCacheEvent).collect { value ->
            apiResult = value
        }

        assertEquals(
            apiResult?.stateMessage?.response?.message,
            LaunchEvents.GetLaunchesApiAndCacheEvent.eventName() + EVENT_CACHE_INSERT_SUCCESS
        )
    }

    @Test
    fun whenGetLaunchItemsFromNetwork_thenItemsCanBeRetrievedFromCache(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(launchList))
        coEvery { networkDataSource.getLaunchList(any()) } returns LAUNCH_LIST
        coEvery { cacheDataSource.insertList(LAUNCH_LIST) } returns longArrayOf(1)
        coEvery { cacheDataSource.getAll() } returns LAUNCH_LIST

        underTest(event = LaunchEvents.GetLaunchesApiAndCacheEvent)
        val results = cacheDataSource.getAll()

        assertTrue(results?.isNotEmpty() == true)
        assertTrue(results?.get(index = 0) is Launch)
    }

    @Test
    fun whenGetLaunchItemsFromNetwork_andNetworkErrorOccurs_thenErrorEventIsEmitted(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).setBody("{}"))
        var apiResult: ApiResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetLaunchesApiAndCacheEvent).collect { value ->
            apiResult = value
        }

        assertEquals(
            apiResult?.stateMessage?.response?.message,
            LaunchEvents.GetLaunchesApiAndCacheEvent.eventName() + EVENT_NETWORK_ERROR
        )
    }

    companion object {
        private val LAUNCH_LIST = listOf(
            Launch(
                type = ViewType.TYPE_LIST,
                daysToFromTitle = 0,
                isLaunchSuccess = 1,
                launchDate = "01/01/2023",
                id = 1,
                launchDaysDifference = "5 days",
                launchSuccessIcon = 0,
                launchYear = "2023",
                launchDateLocalDateTime = LocalDateTime.now(),
                links = Links(
                    articleLink = "articleLink",
                    missionImage = "missionLink",
                    webcastLink = "webCastLink",
                    wikiLink = "wikiLink"
                ),
                missionName = "missionName",
                rocket = Rocket(
                    rocketNameAndType = "rocketNameAndType"
                )
            )
        )
    }
}