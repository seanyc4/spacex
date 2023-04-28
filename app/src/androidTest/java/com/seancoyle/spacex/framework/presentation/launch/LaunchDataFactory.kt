package com.seancoyle.spacex.framework.presentation.launch

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.constants.LaunchNetworkConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch_models.model.launch.*
import com.seancoyle.spacex.R
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchDataFactory
@Inject
constructor(
    private val jsonFileReader: JsonFileReader,
    private val factory: LaunchFactory
) {

    fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()

    fun produceListOfLaunches(): List<LaunchModel> {
        return Gson()
            .fromJson(
                jsonFileReader.readJSONFromAsset("launch_list.json"),
                object : TypeToken<List<LaunchModel>>() {}.type
            )
    }

    fun createLaunchItem(
        id: Int,
        launchDate: String,
        isLaunchSuccess: Int,
        launchSuccessIcon: Int,
        launchDateLocalDateTime: LocalDateTime,
        launchYear: String,
        links: Links,
        missionName: String,
        rocket: Rocket,
        daysToFromTitle: Int,
        launchDaysDifference: String,
        type: Int
    ) = factory.createLaunchItem(
        id = id,
        launchDate = launchDate,
        isLaunchSuccess = isLaunchSuccess,
        launchSuccessIcon = launchSuccessIcon,
        launchDateLocalDateTime = launchDateLocalDateTime,
        launchYear = launchYear,
        links = links,
        missionName = missionName,
        rocket = rocket,
        daysToFromTitle = daysToFromTitle,
        launchDaysDifference = launchDaysDifference,
        type = type,
    )

    fun createLaunchListTest(
        num: Int,
        id: Int?
    ): List<LaunchModel> {
        val list: ArrayList<LaunchModel> = ArrayList()
        for (item in 0 until num) {
            list.add(
                createLaunchItem(
                    id = id ?: UUID.randomUUID().hashCode(),
                    launchDate = UUID.randomUUID().toString(),
                    launchDateLocalDateTime = LocalDateTime.now(),
                    isLaunchSuccess = LAUNCH_SUCCESS,
                    launchSuccessIcon = R.drawable.ic_launch_success,
                    launchYear = UUID.randomUUID().toString(),
                    links = Links(
                        missionImage = DEFAULT_LAUNCH_IMAGE,
                        articleLink = "https://www.google.com",
                        webcastLink = "https://www.youtube.com",
                        wikiLink = "https://www.wikipedia.com"
                    ),
                    missionName = UUID.randomUUID().toString(),
                    rocket = Rocket(
                        rocketNameAndType = UUID.randomUUID().toString()
                    ),
                    daysToFromTitle = UUID.randomUUID().hashCode(),
                    launchDaysDifference = UUID.randomUUID().toString(),
                    type = LaunchType.TYPE_LAUNCH
                )
            )
        }
        return list
    }
}