package com.seancoyle.feature.launch.implementation.data.network.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.network.company.MockWebServerResponseCompany.companyResponse
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
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
internal class CompanyRemoteDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockWebServer : MockWebServer

    @Inject
    lateinit var underTest: CompanyRemoteDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun whenAPISuccessful_getCompanyReturnsCompanyData() = runTest {
        val expectedCompany = Company(
            employees = 7000,
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = 27500000000L,
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(companyResponse)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Success)
        assertEquals(result.data, expectedCompany)
    }

    @Test
    fun whenNetworkFails_getCompanyThrowsException() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkTimesOut_getCompanyReturnsTimeoutError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsNotFound_getCompanyReturnsNotFoundError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsUnauthorized_getCompanyHandlesUnauthorized() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenServerErrors_getCompanyHandlesServerError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenNetworkFails_getCompanyReturnsNetworkError() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsForbidden_getCompanyHandlesForbidden() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_FORBIDDEN)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsRequestTimeout_getCompanyHandlesRequestTimeout() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CLIENT_TIMEOUT)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

    @Test
    fun whenApiReturnsPayloadTooLarge_getCompanyHandlesPayloadTooLarge() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_ENTITY_TOO_LARGE)
        )

        val result = underTest.getCompanyApi()

        assertTrue(result is LaunchResult.Error)
    }

}