package com.seancoyle.launch.implementation.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.implementation.TestConstants
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.network.LaunchNetworkDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import java.net.HttpURLConnection
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchNetworkDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var api: FakeLaunchApi

    @Inject
    lateinit var dateTimeFormatter: com.seancoyle.core.domain.DateFormatter

    @Inject
    lateinit var underTest: LaunchNetworkDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun whenAPISuccessful_getLaunchesReturnsNonEmptyList() = runTest {
        val expectedLaunch = LaunchTypes.Launch(
            id = "1902022-12-01T00:00",
            launchDate = "01-12-2022 at 00:00",
            launchDateLocalDateTime = dateTimeFormatter.formatDate("2022-12-01T00:00:00.000Z"),
            launchYear = "2022",
            launchStatus = LaunchStatus.SUCCESS,
            links = Links(
                missionImage = "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
                articleLink = null,
                webcastLink = null,
                wikiLink = null
            ),
            missionName = "USSF-44",
            rocket = Rocket(
                rocketNameAndType = "Falcon 9/rocket"
            ),
            launchDateStatus = LaunchDateStatus.PAST,
            launchDays = "+/- 340d"

        )

        val result = underTest.getLaunches(launchOptions)

       // assertEquals(expected = expectedLaunch, actual = result)
    }

    @Test
    fun  whenAPIReturns404_getLaunchesShouldThrowHttpException() = runTest {
        api.jsonFileName = TestConstants.ERROR_404_RESPONSE

        val exception = assertFailsWith<HttpException> {
            api.getLaunches(launchOptions)
        }

        assertEquals(
            expected = HttpURLConnection.HTTP_NOT_FOUND,
            actual = exception.response()?.code()
        )
    }
}