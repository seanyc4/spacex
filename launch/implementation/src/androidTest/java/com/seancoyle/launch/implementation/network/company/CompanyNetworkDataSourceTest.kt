package com.seancoyle.launch.implementation.network.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.CompanyFactory
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
    fun getCompanyShouldReturnExpectedCompany() = runTest {
        val expectedCompany = dataFactory.createCompany(
            id = "",
            employees = "7،000",
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = "27،500،000،000"
        )

        val result = underTest.getCompanyInfo()

        assertEquals(expected = expectedCompany, actual = result)
    }

    @Test
    fun getCompanyWhenApiReturns404_shouldThrowHttpException() = runTest {
        api.jsonFileName = COMPANY_404_RESPONSE

        val exception = assertFailsWith<HttpException> {
            api.getCompanyInfo()
        }

        assertEquals(
            expected = HttpURLConnection.HTTP_NOT_FOUND,
            actual = exception.response()?.code()
        )
    }
}