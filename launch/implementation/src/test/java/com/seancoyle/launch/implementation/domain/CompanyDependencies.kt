package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.util.NumberFormatterImpl
import com.seancoyle.core.util.isUnitTest
import com.seancoyle.launch.api.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.data.cache.FakeCompanyInfoCacheDataSourceImpl
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import com.seancoyle.launch.implementation.data.network.FakeCompanyInfoNetworkDataSourceImpl
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

class CompanyDependencies {

    private val numberFormatter = NumberFormatterImpl()
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource
    lateinit var companyInfoNetworkSource: CompanyInfoNetworkDataSource
    lateinit var companyInfoDataFactory: CompanyInfoDataFactory
    lateinit var companyInfoNetworkMapper: CompanyInfoNetworkMapper
    lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    init {
        isUnitTest = true
    }

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->
            companyInfoDataFactory = CompanyInfoDataFactory(classLoader)
        }

        mockWebServer = MockWebServer()
        baseUrl = mockWebServer.url("v3/info/")

        companyInfoNetworkMapper = CompanyInfoNetworkMapper(
            numberFormatter = numberFormatter
        )

        companyInfoCacheDataSource = FakeCompanyInfoCacheDataSourceImpl(
            fakeCompanyInfoDatabase = companyInfoDataFactory.produceFakeCompanyInfoDatabase(
                companyInfoDataFactory.produceCompanyInfo()
            )
        )

        companyInfoNetworkSource = FakeCompanyInfoNetworkDataSourceImpl(
            networkMapper = companyInfoNetworkMapper,
            baseUrl = baseUrl,

            )
    }

}

















