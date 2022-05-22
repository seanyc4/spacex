package com.seancoyle.spacex.framework.datasource.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.launch.*
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_SUCCESS
import com.seancoyle.spacex.util.JsonFileReader
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class LaunchDataFactory
@Inject
constructor(
    private val jsonFileReader: JsonFileReader,
    private val factory: LaunchFactory
) {

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
                        videoLink = "https://www.youtube.com",
                        wikipedia = "https://www.wikipedia.com"
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













