package com.seancoyle.feature.launch.implementation.data.network

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRemoteDataSource
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

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Success)
        assertEquals(2, result.data.size)

        // Verify first launch
        val firstLaunch = result.data[0]
        assertEquals("test-launch-1", firstLaunch.id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", firstLaunch.name)
        assertEquals("https://lldev.thespacedevs.com/2.3.0/launches/test-launch-1/", firstLaunch.url)
        assertEquals("list", firstLaunch.responseMode)
        assertEquals(LaunchStatus.SUCCESS.name, firstLaunch.launchStatus.name)
        assertEquals("2025-12-13T05:34:00Z", firstLaunch.net)
        assertEquals("Starlink night fairing", firstLaunch.image?.name)
        assertEquals(false, firstLaunch.webcastLive)

        // Verify second launch
        val secondLaunch = result.data[1]
        assertEquals("test-launch-2", secondLaunch.id)
        assertEquals("Falcon Heavy | Mission 2", secondLaunch.name)
        assertEquals(true, secondLaunch.webcastLive)
    }

    @Test
    fun whenNetworkFails_getLaunchesThrowsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkTimesOut_getLaunchesReturnsTimeoutError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsNotFound_getLaunchesReturnsNotFoundError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsUnauthorized_getLaunchesHandlesUnauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenServerErrors_getLaunchesHandlesServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkFails_getLaunchesReturnsNetworkError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsForbidden_getLaunchesHandlesForbidden() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsRequestTimeout_getLaunchesHandlesRequestTimeout() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsPayloadTooLarge_getLaunchesHandlesPayloadTooLarge() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
        )

        val result = underTest.getLaunches(offset = 0)

        assertTrue(result is LaunchResult.Error)
    }
}
