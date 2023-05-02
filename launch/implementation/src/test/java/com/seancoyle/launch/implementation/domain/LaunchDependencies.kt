package com.seancoyle.launch.implementation.domain

import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_OPTIONS_ROCKET
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_OPTIONS_SORT
import com.seancoyle.core.util.DateFormatConstants.YYYY_MM_DD_HH_MM_SS
import com.seancoyle.core.util.DateFormatterImpl
import com.seancoyle.core.util.DateTransformerImpl
import com.seancoyle.core.util.isUnitTest
import com.seancoyle.launch.api.LaunchCacheDataSource
import com.seancoyle.launch.api.LaunchNetworkDataSource
import com.seancoyle.launch.api.model.LaunchFactory
import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.api.model.Options
import com.seancoyle.launch.api.model.Populate
import com.seancoyle.launch.api.model.Select
import com.seancoyle.launch.api.model.Sort
import com.seancoyle.launch.implementation.data.cache.FakeLaunchCacheDataSourceImpl
import com.seancoyle.launch.implementation.data.network.FakeLaunchNetworkDataSourceImpl
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class LaunchDependencies {

    private val dateFormat = DateTimeFormatter.ofPattern(
        YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH
    ).withZone(ZoneId.systemDefault())

    private val dateFormatter = DateFormatterImpl(dateFormat)
    private val dateTransformer = DateTransformerImpl()
    lateinit var launchCacheDataSource: LaunchCacheDataSource
    lateinit var launchFactory: LaunchFactory
    lateinit var launchDataFactory: LaunchDataFactory
    lateinit var launchOptions: LaunchOptions
    lateinit var networkDataSource: LaunchNetworkDataSource
    lateinit var networkMapper: LaunchNetworkMapper
    lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    init {
        isUnitTest = true
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

















