package com.seancoyle.launch.implementation.domain

import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_OPTIONS_ROCKET
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_OPTIONS_SORT
import com.seancoyle.core.presentation.util.DateFormatConstants.YYYY_MM_DD_HH_MM_SS
import com.seancoyle.core.presentation.util.DateFormatter
import com.seancoyle.core.presentation.util.DateTransformer
import com.seancoyle.core.util.isUnitTest
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.model.Options
import com.seancoyle.launch.api.domain.model.Populate
import com.seancoyle.launch.api.domain.model.Select
import com.seancoyle.launch.api.domain.model.Sort
import com.seancoyle.launch.implementation.data.cache.FakeLaunchCacheDataSourceImpl
import com.seancoyle.launch.implementation.data.network.FakeLaunchNetworkDataSourceImpl
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class LaunchDependencies {

    private val dateFormat = DateTimeFormatter.ofPattern(
        YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH
    ).withZone(ZoneId.systemDefault())

    @MockK
    private lateinit var dateFormatter: DateFormatter
    @MockK
    private lateinit var dateTransformer:  DateTransformer

    lateinit var launchCacheDataSource: LaunchCacheDataSource
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
        MockKAnnotations.init(this)

        this.javaClass.classLoader?.let { classLoader ->
            launchDataFactory = LaunchDataFactory(classLoader)
        }

        mockWebServer = MockWebServer()
        baseUrl = mockWebServer.url("v3/launches/")

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

















