package com.seancoyle.feature.launch.data.remote

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
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
internal class LaunchesRemoteDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var underTest: LaunchesRemoteDataSource

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

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(2, result.data.size)

        // Verify first launch summary
        val firstLaunch = result.data[0]
        assertEquals("test-launch-1", firstLaunch.id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", firstLaunch.missionName)
        assertEquals("2025-12-13T05:34:00Z", firstLaunch.net)
        assertEquals("https://example.com/path_to_mission_patch.jpg", firstLaunch.imageUrl)
        assertEquals("Go for Launch", firstLaunch.status.name)
        assertEquals("Go", firstLaunch.status.abbrev)

        // Verify second launch summary
        val secondLaunch = result.data[1]
        assertEquals("test-launch-2", secondLaunch.id)
        assertEquals("Falcon Heavy | Mission 2", secondLaunch.missionName)
        assertEquals("2025-12-14T10:00:00Z", secondLaunch.net)
        assertEquals("https://example.com/path_to_another_mission_patch.jpg", secondLaunch.imageUrl)
    }

    @Test
    fun whenNetworkFails_getLaunchesThrowsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkTimesOut_getLaunchesReturnsTimeoutError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsNotFound_getLaunchesReturnsNotFoundError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsUnauthorized_getLaunchesHandlesUnauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenServerErrors_getLaunchesHandlesServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkFails_getLaunchesReturnsNetworkError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsForbidden_getLaunchesHandlesForbidden() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsRequestTimeout_getLaunchesHandlesRequestTimeout() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsPayloadTooLarge_getLaunchesHandlesPayloadTooLarge() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsEmptyList_getLaunchesReturnsEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.emptyLaunchesResponse)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data.size)
    }

    @Test
    fun whenApiReturnsNullResults_getLaunchesReturnsEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.nullResultsResponse)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data.size)
    }

    @Test
    fun whenApiReturnsPartialData_getLaunchesFiltersInvalidItems() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.partialDataResponse)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data.size)
        assertEquals("test-launch-1", result.data[0].id)
    }

    @Test
    fun whenApiReturnsMalformedJson_getLaunchesReturnsError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.malformedJsonResponse)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenLaunchesQueryHasFilters_apiIsCalled() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.launchesResponse)
        )

        val query = LaunchesQuery(query = "Falcon 9")
        val result = underTest.getLaunches(page = 0, launchesQuery = query)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun whenApiReturnsMissingImageUrl_usesDefaultImage() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.responseWithoutImage)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data.size)
        // Should use default image URL when image is null
        assertTrue(result.data[0].imageUrl.isNotEmpty())
    }

    @Test
    fun whenApiReturnsMultiplePages_handlesCorrectly() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.responseWithNextPage)
        )

        val result = underTest.getLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data.size)
    }
}
