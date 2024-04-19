package com.seancoyle.feature.launch.implementation.presentation

import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import com.seancoyle.feature.launch.implementation.data.network.company.MockWebServerResponseCompany
import com.seancoyle.feature.launch.implementation.data.network.launch.MockWebServerResponseLaunches
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
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
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Inject
    internal lateinit var getCompanyApiAndCacheUseCase: GetCompanyApiAndCacheUseCase

    @Inject
    internal lateinit var getLaunchesApiAndCacheUseCase: GetCompanyApiAndCacheUseCase
   /* @Inject
    internal lateinit var launchCacheDataSource: LaunchCacheDataSource

    @Inject
    internal lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @Inject
    internal lateinit var companyCacheDataSource: CompanyCacheDataSource

    @Inject
    internal lateinit var companyInfoNetworkDataSource: CompanyNetworkDataSource*/

    @Inject
    internal lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var mockWebServer: MockWebServer

    protected val launchGridTag = "Launch Grid"

    @Before
    @CallSuper
    fun setup() {
        hiltRule.inject()
        Intents.init()
        getTestDataAndInsertToFakeDatabase()
    }

    @After
    @CallSuper
    @Throws(Exception::class)
    fun tearDown() {
        Intents.release()
        mockWebServer.shutdown()
    }

    private fun getTestDataAndInsertToFakeDatabase() = runTest {
      /*  launchCacheDataSource.deleteAll()
        companyCacheDataSource.deleteAll()*/
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

        getCompanyApiAndCacheUseCase.invoke()
        getLaunchesApiAndCacheUseCase.invoke()
    }
}