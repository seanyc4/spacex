package com.seancoyle.feature.launch.implementation.data.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.network.LaunchNetworkDataSource
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
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchNetworkDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var underTest: LaunchNetworkDataSource

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

        assertTrue(result is Result.Success)

        // We skip checking item.launchDays as its dynamic and changes based on the date
        result.data.forEachIndexed { index, item ->
            assertEquals(item.id, expectedLaunches[index].id)
            assertEquals(item.launchDateLocalDateTime, expectedLaunches[index].launchDateLocalDateTime)
            assertEquals(item.launchDate, expectedLaunches[index].launchDate)
            assertEquals(item.launchDateStatus, expectedLaunches[index].launchDateStatus)
            assertEquals(item.launchYear, expectedLaunches[index].launchYear)
            assertEquals(item.launchStatus, expectedLaunches[index].launchStatus)
            assertEquals(item.links, expectedLaunches[index].links)
            assertEquals(item.missionName, expectedLaunches[index].missionName)
            assertEquals(item.rocket, expectedLaunches[index].rocket)
        }
    }

    @Test
    fun whenNetworkFails_getLaunchesThrowsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
    }

    @Test
    fun whenNetworkTimesOut_getLaunchesReturnsTimeoutError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_TIMEOUT, result.error)
    }

    @Test
    fun whenApiReturnsNotFound_getLaunchesReturnsNotFoundError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_NOT_FOUND, result.error)
    }

    @Test
    fun whenApiReturnsUnauthorized_getLaunchesHandlesUnauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_UNAUTHORIZED, result.error)
    }

    @Test
    fun whenServerErrors_getLaunchesHandlesServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_INTERNAL_SERVER_ERROR, result.error)
    }

    @Test
    fun whenNetworkFails_getLaunchesReturnsNetworkError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_CONNECTION_FAILED, result.error)
    }

    @Test
    fun whenApiReturnsForbidden_getLaunchesHandlesForbidden() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_FORBIDDEN, result.error)
    }

    @Test
    fun whenApiReturnsRequestTimeout_getLaunchesHandlesRequestTimeout() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_TIMEOUT, result.error)
    }

    @Test
    fun whenApiReturnsPayloadTooLarge_getLaunchesHandlesPayloadTooLarge() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
        )

        val result = underTest.getLaunches(launchOptions)

        assertTrue(result is Result.Error)
        assertEquals(DataError.NETWORK_PAYLOAD_TOO_LARGE, result.error)
    }

    private val expectedLaunches = listOf(
        LaunchTypes.Launch(
            id = "1022022-09-15T12:14",
            launchDate = "15-09-2022 at 12:14",
            launchDateLocalDateTime = LocalDateTime.of(2022, 9, 15, 12, 14),
            launchYear = "2022",
            launchStatus = LaunchStatus.SUCCESS,
            links = Links(
                missionImage = "https://example.com/path_to_mission_patch.jpg",
                articleLink = "https://example.com/path_to_article",
                webcastLink = "https://example.com/path_to_webcast",
                wikiLink = "https://en.wikipedia.org/wiki/SpaceX_CRS-22"
            ),
            missionName = "CRS-22",
            rocket = Rocket(
                rocketNameAndType = "Falcon 9/FT"
            ),
            launchDateStatus = LaunchDateStatus.PAST,
            launchDays = "+/- 579d",
            launchDaysResId = 0,
            launchStatusIconResId = 0
        ),
        LaunchTypes.Launch(
            id = "1032022-10-04T15:22",
            launchDate = "04-10-2022 at 15:22",
            launchDateLocalDateTime = LocalDateTime.of(2022, 10, 4, 15, 22),
            launchStatus = LaunchStatus.FAILED,
            launchYear = "2022",
            links = Links(
                missionImage = "https://example.com/path_to_another_mission_patch.jpg",
                articleLink = "https://example.com/path_to_another_article",
                webcastLink = "https://example.com/path_to_another_webcast",
                wikiLink = "https://en.wikipedia.org/wiki/SpaceX_CRS-23"
            ),
            missionName = "CRS-23",
            rocket = Rocket(
                rocketNameAndType = "Falcon Heavy/Block 5"
            ),
            launchDateStatus = LaunchDateStatus.PAST,
            launchDays = "+/- 560d",
            launchDaysResId = 0,
            launchStatusIconResId = 0
        )
    )
}