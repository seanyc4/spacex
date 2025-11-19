package com.seancoyle.feature.launch.api.domain.model

import java.time.LocalDateTime

sealed interface LaunchTypes {
    data class CompanySummary(
        val id: String,
        val summary: String,
        val name: String,
        val founder: String,
        val founded: Int,
        val employees: String,
        val launchSites: Int,
        val valuation: String
    ) : LaunchTypes

    data class SectionTitle(
        val id: String,
        val title: String
    ) : LaunchTypes

    data class Grid(
        val id: String,
        val items: RocketWithMission
    ) : LaunchTypes

    data class Carousel(
        val id: String,
        val items: List<RocketWithMission>
    ) : LaunchTypes

    data class Launch(
        val id: String,
        val launchDate: String,
        val launchDateLocalDateTime: LocalDateTime,
        val launchYear: String,
        val launchStatus: LaunchStatus,
        val links: Links,
        val missionName: String,
        val rocket: Rocket,
        val launchDateStatus: LaunchDateStatus,
        val launchDays: String,
        val isLaunchSuccess: Boolean
    ) : LaunchTypes
}

data class Links(
    val missionImage: String,
    val articleLink: String?,
    val webcastLink: String?,
    val wikiLink: String?,
)

data class Rocket(
    val rocketNameAndType: String,
)

data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)
