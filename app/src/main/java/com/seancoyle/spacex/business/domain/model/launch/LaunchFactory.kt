package com.seancoyle.spacex.business.domain.model.launch

import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.launch.LaunchType.Companion.TYPE_LAUNCH
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.DEFAULT_LAUNCH_IMAGE
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
        isLaunchSuccess: Boolean,
        launchSuccessIcon: Int,
        launchYear: String,
        links: Links,
        missionName: String,
        rocket: Rocket,
        daysToFromTitle: Int,
        launchDaysDifference: String,
        type: Int
    ): LaunchDomainEntity {
        return LaunchDomainEntity(
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

    fun createLaunchList(
        launchList: List<LaunchDomainEntity>
    ): List<LaunchDomainEntity> {
        val list: ArrayList<LaunchDomainEntity> = ArrayList()
        for (item in launchList) {
            list.add(
                createLaunchItem(
                    id = item.id,
                    launchDate = item.launchDate,
                    launchDateLocalDateTime = item.launchDateLocalDateTime,
                    isLaunchSuccess = item.isLaunchSuccess,
                    launchSuccessIcon = item.launchSuccessIcon,
                    launchYear = item.launchYear,
                    links = item.links,
                    missionName = item.missionName,
                    rocket = item.rocket,
                    daysToFromTitle = item.daysToFromTitle,
                    launchDaysDifference = item.launchDaysDifference,
                    type = item.type
                )
            )
        }
        return list
    }

    fun createLaunchListTest(
        num: Int,
        id: Int? = 1
    ): List<LaunchDomainEntity> {
        val list: ArrayList<LaunchDomainEntity> = ArrayList()
        for (item in 0 until num) {
            list.add(
                createLaunchItem(
                    id = id ?: 1,
                    launchDate = UUID.randomUUID().toString(),
                    launchDateLocalDateTime = LocalDateTime.now(),
                    isLaunchSuccess = true,
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
                    type = TYPE_LAUNCH
                )
            )
        }
        return list
    }
}









