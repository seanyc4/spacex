package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.CompanyInfoDataFactory
import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.cache.company.FakeCompanyInfoCacheDataSourceImpl
import com.seancoyle.launch_datasource.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.company.FakeCompanyInfoNetworkDataSourceImpl
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_datasource.network.implementation.numberformatter.NumberFormatterImpl
import com.seancoyle.launch_datasource.network.mappers.company.CompanyInfoNetworkMapper
import com.seancoyle.core.util.isUnitTest
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

class CompanyDependencies {

    private val numberFormatter = NumberFormatterImpl()
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource
    lateinit var companyInfoNetworkSource: CompanyInfoNetworkDataSource
    lateinit var companyInfoFactory: CompanyInfoFactory
    lateinit var companyInfoDataFactory: CompanyInfoDataFactory
    lateinit var companyInfoNetworkMapper: CompanyInfoNetworkMapper
    lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->
            companyInfoDataFactory = CompanyInfoDataFactory(classLoader)
        }

        mockWebServer = MockWebServer()
        baseUrl = mockWebServer.url("v3/info/")
        companyInfoFactory = CompanyInfoFactory()

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

















