package com.seancoyle.spacex

import com.seancoyle.launch.contract.domain.model.Launch
import com.seancoyle.launch.contract.domain.model.Links
import com.seancoyle.launch.contract.domain.model.Rocket
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchFactory
@Inject
constructor(){

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
    ): Launch {
        return Launch(
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
}









