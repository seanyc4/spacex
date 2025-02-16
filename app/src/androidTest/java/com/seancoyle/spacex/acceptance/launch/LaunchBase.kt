package com.seancoyle.spacex.acceptance.launch

import androidx.annotation.CallSuper
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import com.seancoyle.spacex.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.net.HttpURLConnection
import javax.inject.Inject

@HiltAndroidTest
open class LaunchBase {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Inject
    lateinit var mockWebServer: MockWebServer

    protected val launchGridTag = "Launch Grid"

    @Before
    @CallSuper
    fun setup() {
        hiltRule.inject()
        Intents.init()
        setupMockWebServer()
    }

    @After
    @CallSuper
    @Throws(Exception::class)
    fun tearDown() {
        Intents.release()
        mockWebServer.shutdown()
    }

    private fun setupMockWebServer() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseCompany.companyResponse)
        )
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseLaunches.launchesResponse)
        )
    }
}