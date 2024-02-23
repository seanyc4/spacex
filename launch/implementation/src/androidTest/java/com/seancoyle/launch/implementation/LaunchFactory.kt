package com.seancoyle.launch.implementation

import com.seancoyle.launch.implementation.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.Links
import com.seancoyle.launch.implementation.domain.model.Rocket
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchFactory @Inject constructor() {

    fun provideValidFilterYearDates() = listOf(
        "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
        "2012", "2010", "2009", "2008", "2007", "2006"
    ).shuffled()

    fun createLaunchItem(
        id: String,
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