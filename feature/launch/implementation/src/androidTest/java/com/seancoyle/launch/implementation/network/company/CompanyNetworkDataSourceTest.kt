package com.seancoyle.launch.implementation.network.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.launch.implementation.CompanyFactory
import com.seancoyle.launch.implementation.TestConstants.ERROR_404_RESPONSE
import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
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
@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
internal class CompanyNetworkDataSourceTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var dataFactory: CompanyFactory

    @Inject
    lateinit var api: FakeCompanyApi

    @Inject
    lateinit var underTest: CompanyInfoNetworkDataSource

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun whenAPISuccessful_getCompanyReturnsCompanyData() = runTest {
        val expectedCompany = dataFactory.createCompany(
            id = "",
            employees = "7،000",
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = "27،500،000،000"
        )

        val result = underTest.getCompany()

        //assertEquals(expected = expectedCompany, actual = result)
    }

    @Test
    fun whenAPIReturns404_getCompanyShouldThrowHttpException() = runTest {
        api.jsonFileName = ERROR_404_RESPONSE

        val exception = assertFailsWith<HttpException> {
            api.getCompany()
        }

        assertEquals(
            expected = HttpURLConnection.HTTP_NOT_FOUND,
            actual = exception.response()?.code()
        )
    }
}