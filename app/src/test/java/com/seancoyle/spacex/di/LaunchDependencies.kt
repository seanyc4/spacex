package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.LaunchDataFactory
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.launch.FakeLaunchCacheDataSourceImpl
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.data.network.launch.FakeLaunchNetworkDataSourceImpl
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.datasource.cache.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.spacex.framework.datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.model.launch.*
import com.seancoyle.spacex.util.isUnitTest

import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class LaunchDependencies {

    private val dateFormat = DateTimeFormatter.ofPattern(
        YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH
    ).withZone(ZoneId.systemDefault())

    val dateFormatter = DateFormatterImpl(dateFormat)
    val dateTransformer = DateTransformerImpl()
    lateinit var launchCacheDataSource: LaunchCacheDataSource
    lateinit var launchFactory: LaunchFactory
    lateinit var launchDataFactory: LaunchDataFactory
    lateinit var launchOptions: LaunchOptions
    lateinit var networkDataSource: LaunchNetworkDataSource
    lateinit var networkMapper: LaunchNetworkMapper
    lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {

        this.javaClass.classLoader?.let { classLoader ->
            launchDataFactory = LaunchDataFactory(classLoader)
        }

        mockWebServer = MockWebServer()
        baseUrl = mockWebServer.url("v3/launches/")
        launchFactory = LaunchFactory()

        networkMapper = LaunchNetworkMapper(
            dateFormatter = dateFormatter,
            dateTransformer = dateTransformer
        )

        networkDataSource = FakeLaunchNetworkDataSourceImpl(
            baseUrl = baseUrl,
            networkMapper = networkMapper
        )

        launchCacheDataSource = FakeLaunchCacheDataSourceImpl(
            fakeLaunchDatabase = launchDataFactory.produceFakeAppDatabase(
                launchDataFactory.produceListOfLaunchItems()
            ),
        )

        // Launch options dependency
        launchOptions = LaunchOptions(
            options = Options(
                populate = listOf(
                    Populate(
                        path = LAUNCH_OPTIONS_ROCKET,
                        select = Select(
                            name = 1,
                            type = 2
                        )
                    )
                ),
                sort = Sort(
                    flight_number = LAUNCH_OPTIONS_SORT,
                ),
                limit = 500
            )
        )

    }

}

















