package com.seancoyle.launch_domain.model.launch

import com.seancoyle.launch_domain.model.launch.LaunchType.Companion.TYPE_LAUNCH
import java.time.LocalDateTime
import java.util.*
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class LaunchFactory {

    fun createLaunchItem(
        id: Int,
        launchDate: String,
        launchDateLocalDateTime: LocalDateTime,
        isLaunchSuccess: Int?,
        launchSuccessIcon: Int,
        launchYear: String,
        links: Links,
        missionName: String,
        rocket: Rocket,
        daysToFromTitle: Int,
        launchDaysDifference: String,
        type: Int
    ): LaunchModel {
        return LaunchModel(
            id = id,
            launchDate = launchDate,
            launchDateLocalDateTime = launchDateLocalDateTime,
            isLaunchSuccess = isLaunchSuccess,
            launchSuccessIcon = launchSuccessIcon,
            launchYear = launchYear,
            links = links,
            missionName = missionName,
            rocket = rocket,
            daysToFromTitle = daysToFromTitle,
            launchDaysDifference = launchDaysDifference,
            type = type
        )
    }

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
                    isLaunchSuccess = 2,
                    launchSuccessIcon = 0,
                    launchYear = UUID.randomUUID().toString(),
                    links = Links(
                        missionImage = "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
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
                    type = TYPE_LAUNCH
                )
            )
        }
        return list
    }
}









