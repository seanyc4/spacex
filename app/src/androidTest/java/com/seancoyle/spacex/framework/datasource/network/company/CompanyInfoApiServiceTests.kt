package com.seancoyle.spacex.framework.datasource.network.company

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.di.CompanyInfoModule
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.implementation.company.CompanyInfoRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@HiltAndroidTest
@UninstallModules(
    CompanyInfoModule::class
)
class CompanyInfoApiServiceTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var apiService: CompanyInfoRetrofitService

    // dependencies
    @Inject
    lateinit var api: CompanyInfoApi

    @Inject
    lateinit var networkMapper: CompanyInfoNetworkMapper

    @Inject
    lateinit var dataFactory: CompanyInfoDataFactory


    @Before
    fun init() {
        hiltRule.inject()
        apiService = CompanyInfoRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

    @Test
    fun getCompanyInfoFromNetwork_confirmExpectedResult() = runBlocking {

        val expectedResult = dataFactory.createCompanyInfo(
            id = "",
            employees = "7،000",
            founded = 2002,
            founder = "Elon Musk",
            launchSites = 3,
            name = "SpaceX",
            valuation = "27،500،000،000"
        )

        val result = apiService.getCompanyInfo()

        assert(result == expectedResult)
    }

}