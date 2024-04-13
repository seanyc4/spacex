package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core_testing.MainCoroutineRule
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import com.seancoyle.launch.implementation.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.model.Links
import com.seancoyle.launch.implementation.domain.model.Rocket
import com.seancoyle.launch.implementation.domain.network.LaunchNetworkDataSource
import com.seancoyle.launch.implementation.network.MockWebServerResponseLaunchList.launchList
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
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class GetLaunchesApiAndCacheUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var cacheDataSource: LaunchCacheDataSource

    @MockK
    private lateinit var networkDataSource: LaunchNetworkDataSource

    @MockK
    private lateinit var launchOptions: LaunchOptions

    private lateinit var mockWebServer: MockWebServer

    private lateinit var underTest: GetLaunchesApiAndCacheUseCaseImpl

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        mockWebServer = MockWebServer()
        underTest =
            GetLaunchesApiAndCacheUseCaseImpl(
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
        coEvery { networkDataSource.getLaunches(any()) } returns LAUNCH_LIST
        coEvery { cacheDataSource.insertList(LAUNCH_LIST) } returns longArrayOf(1)

        var dataResult: com.seancoyle.core.common.result.DataResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetLaunchesApiAndCacheEvent).collect { value ->
            dataResult = value
        }

        assertEquals(
            dataResult?.stateMessage?.response?.message,
            LaunchEvents.GetLaunchesApiAndCacheEvent.eventName() + EVENT_CACHE_INSERT_SUCCESS
        )
    }

    @Test
    fun whenGetLaunchItemsFromNetwork_thenItemsCanBeRetrievedFromCache(): Unit = runBlocking {

        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(launchList))
        coEvery { networkDataSource.getLaunches(any()) } returns LAUNCH_LIST
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
        var dataResult: com.seancoyle.core.common.result.DataResult<LaunchState>? = null

        underTest(event = LaunchEvents.GetLaunchesApiAndCacheEvent).collect { value ->
            dataResult = value
        }

        assertEquals(
            dataResult?.stateMessage?.response?.message,
            LaunchEvents.GetLaunchesApiAndCacheEvent.eventName() + EVENT_NETWORK_ERROR
        )
    }

    companion object {
        private val LAUNCH_LIST = listOf(
            Launch(
                type = ViewType.TYPE_LIST,
                launchDateStatus = 0,
                isLaunchSuccess = 1,
                launchDate = "01/01/2023",
                id = 1,
                launchDays = "5 days",
                launchStatus = 0,
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