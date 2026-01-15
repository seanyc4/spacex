package com.seancoyle.feature.launch.data.remote

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
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
import kotlin.test.assertNotNull
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
    fun whenAPISuccessful_getUpcomingDetailedLaunchesReturnsNonEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.launchesResponse)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        val detailedResult = result.data
        assertEquals(2, detailedResult.summaries.size)
        assertEquals(2, detailedResult.details.size)

        // Verify first launch summary
        val firstSummary = detailedResult.summaries[0]
        assertEquals("test-launch-1", firstSummary.id)
        assertEquals("Falcon 9 Block 5 | Starlink Group 15-12", firstSummary.missionName)
        assertEquals("2025-12-13T05:34:00Z", firstSummary.net)
        assertEquals("https://example.com/path_to_mission_patch.jpg", firstSummary.imageUrl)
        assertEquals("Go for Launch", firstSummary.status.name)
        assertEquals("Go", firstSummary.status.abbrev)

        // Verify second launch summary
        val secondSummary = detailedResult.summaries[1]
        assertEquals("test-launch-2", secondSummary.id)
        assertEquals("Falcon Heavy | Mission 2", secondSummary.missionName)
        assertEquals("2025-12-14T10:00:00Z", secondSummary.net)
        assertEquals("https://example.com/path_to_another_mission_patch.jpg", secondSummary.imageUrl)
    }

    @Test
    fun whenAPISuccessful_getPastDetailedLaunchesReturnsNonEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.launchesResponse)
        )

        val result = underTest.getPastDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        val detailedResult = result.data
        assertEquals(2, detailedResult.summaries.size)
        assertEquals(2, detailedResult.details.size)
    }

    @Test
    fun whenNetworkFails_getUpcomingDetailedLaunchesThrowsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkTimesOut_getUpcomingDetailedLaunchesReturnsError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsNotFound_getUpcomingDetailedLaunchesReturnsError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsUnauthorized_getUpcomingDetailedLaunchesHandlesUnauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenServerErrors_getUpcomingDetailedLaunchesHandlesServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkFails_getUpcomingDetailedLaunchesReturnsNetworkError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsForbidden_getUpcomingDetailedLaunchesHandlesForbidden() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsRequestTimeout_getUpcomingDetailedLaunchesHandlesRequestTimeout() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsPayloadTooLarge_getUpcomingDetailedLaunchesHandlesPayloadTooLarge() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsEmptyList_getUpcomingDetailedLaunchesReturnsEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.emptyLaunchesResponse)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data.summaries.size)
    }

    @Test
    fun whenApiReturnsNullResults_getUpcomingDetailedLaunchesReturnsEmptyList() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.nullResultsResponse)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(0, result.data.summaries.size)
    }

    @Test
    fun whenApiReturnsPartialData_getUpcomingDetailedLaunchesFiltersInvalidItems() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.partialDataResponse)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data.summaries.size)
        assertEquals("test-launch-1", result.data.summaries[0].id)
    }

    @Test
    fun whenApiReturnsMalformedJson_getUpcomingDetailedLaunchesReturnsError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.malformedJsonResponse)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

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
        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = query)

        assertTrue(result is LaunchResult.Success)
    }

    @Test
    fun whenApiReturnsMissingImageUrl_usesDefaultImage() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.responseWithoutImage)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data.summaries.size)
        // Should use default image URL when image is null
        assertTrue(result.data.summaries[0].imageUrl.isNotEmpty())
    }

    @Test
    fun whenApiReturnsMultiplePages_handlesCorrectly() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.responseWithNextPage)
        )

        val result = underTest.getUpcomingDetailedLaunches(page = 0, launchesQuery = LaunchesQuery())

        assertTrue(result is LaunchResult.Success)
        assertEquals(1, result.data.summaries.size)
    }

    @Test
    fun getLaunch_whenApiSuccessful_returnsLaunch() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.singleLaunchResponse)
        )

        val result = underTest.getLaunch(id = "test-launch-1", launchType = LaunchesType.UPCOMING)

        assertTrue(result is LaunchResult.Success)
        assertNotNull(result.data)
        assertEquals("test-launch-1", result.data.id)
    }

    @Test
    fun getLaunch_whenApiReturnsError_returnsError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getLaunch(id = "non-existent", launchType = LaunchesType.UPCOMING)

        assertTrue(result is LaunchResult.Error)
    }
}
