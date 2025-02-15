package com.seancoyle.feature.launch.implementation.data.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchRemoteDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchRemoteDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var underTest: LaunchRemoteDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun whenAPISuccessful_getLaunchesReturnsNonEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.launchesResponse)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isSuccess)

        // We skip checking item.launchDays as its dynamic and changes based on the date
        result.getOrNull()!!.launches.forEachIndexed { index, item ->
            assertEquals(item.flightNumber, expectedLaunches.launches[index].flightNumber)
            assertEquals(item.launchDate, expectedLaunches.launches[index].launchDate)
            assertEquals(item.links, expectedLaunches.launches[index].links)
            assertEquals(item.missionName, expectedLaunches.launches[index].missionName)
            assertEquals(item.rocket, expectedLaunches.launches[index].rocket)
            assertEquals(item.isLaunchSuccess, expectedLaunches.launches[index].isLaunchSuccess)
        }
    }

    @Test
    fun whenNetworkFails_getLaunchesThrowsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenNetworkTimesOut_getLaunchesReturnsTimeoutError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenApiReturnsNotFound_getLaunchesReturnsNotFoundError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenApiReturnsUnauthorized_getLaunchesHandlesUnauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenServerErrors_getLaunchesHandlesServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenNetworkFails_getLaunchesReturnsNetworkError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenApiReturnsForbidden_getLaunchesHandlesForbidden() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenApiReturnsRequestTimeout_getLaunchesHandlesRequestTimeout() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    @Test
    fun whenApiReturnsPayloadTooLarge_getLaunchesHandlesPayloadTooLarge() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result.isFailure)
    }

    private val expectedLaunches = LaunchesDto(
        listOf(LaunchDto(
            launchDate = "2022-09-15T11:14:00.000Z",
            links = LinksDto(
                articleLink = "https://example.com/path_to_article",
                webcastLink = "https://example.com/path_to_webcast",
                wikiLink = "https://en.wikipedia.org/wiki/SpaceX_CRS-22",
                patch = PatchDto(
                    missionImage = "https://example.com/path_to_mission_patch.jpg"
                )
            ),
            missionName = "CRS-22",
            rocket = RocketDto(
                name = "Falcon 9",
                type = "FT"
            ),
            flightNumber = 102,
            isLaunchSuccess = true
        ),
            LaunchDto(
            launchDate = "2022-10-04T14:22:00.000Z",
            links = LinksDto(
                articleLink = "https://example.com/path_to_another_article",
                webcastLink = "https://example.com/path_to_another_webcast",
                wikiLink = "https://en.wikipedia.org/wiki/SpaceX_CRS-23",
                patch = PatchDto(
                    missionImage = "https://example.com/path_to_another_mission_patch.jpg"
                )
            ),
            missionName = "CRS-23",
            rocket = RocketDto(
                name = "Falcon Heavy",
                type = "Block 5"
            ),
            flightNumber = 103,
            isLaunchSuccess = false
        )
    )
    )
}