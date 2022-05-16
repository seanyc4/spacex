package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.CompanyInfoDataFactory
import com.seancoyle.spacex.business.data.LaunchDataFactory
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.company.FakeCompanyInfoCacheDataSourceImpl
import com.seancoyle.spacex.business.data.cache.launch.FakeLaunchCacheDataSourceImpl
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.datasource.cache.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.spacex.framework.datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.implementation.numberformatter.NumberFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.model.launch.*
import com.seancoyle.spacex.util.isUnitTest
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DependencyContainer {

    private val dateFormat = DateTimeFormatter.ofPattern(
        YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH
    ).withZone(ZoneId.systemDefault())

    val dateFormatter = DateFormatterImpl(dateFormat)
    val dateTransformer = DateTransformerImpl()
    val numberFormatter = NumberFormatterImpl()
    lateinit var launchCacheDataSource: LaunchCacheDataSource
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource
    lateinit var launchFactory: LaunchFactory
    lateinit var companyInfoFactory: CompanyInfoFactory
    lateinit var launchDataFactory: LaunchDataFactory
    lateinit var companyInfoDataFactory: CompanyInfoDataFactory
    lateinit var launchOptions: LaunchOptions

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->
            launchDataFactory = LaunchDataFactory(classLoader)
        }

        this.javaClass.classLoader?.let { classLoader ->
            companyInfoDataFactory = CompanyInfoDataFactory(classLoader)
        }

        launchFactory = LaunchFactory()
        companyInfoFactory = CompanyInfoFactory()

        launchCacheDataSource = FakeLaunchCacheDataSourceImpl(
            fakeLaunchDatabase = launchDataFactory.produceFakeAppDatabase(
                launchDataFactory.produceListOfLaunchItems()
            ),
        )

        companyInfoCacheDataSource = FakeCompanyInfoCacheDataSourceImpl(
            fakeCompanyInfoDatabase = companyInfoDataFactory.produceFakeCompanyInfoDatabase(
                companyInfoDataFactory.produceCompanyInfo()
            )
        )

        // Launch options dependency
        launchOptions = LaunchOptions(
            options = Options(
                populate = listOf(
                    Populate(
                        path = LAUNCH_OPTIONS_ROCKET,
                        select = Select(
                            name = 1,
                            type =2
                        )
                    )
                ),
                sort = Sort(
                    flight_number = LAUNCH_OPTIONS_SORT,
                ),
                limit =500
            )
        )
    }

}

















