package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

sealed class LaunchTypes {
    data class CompanySummary(
        val id: String,
        val company: Company
    ) : LaunchTypes()

    data class SectionTitle(
        val id: String,
        val title: String
    ) : LaunchTypes()

    data class Grid(
        val id: String,
        val links: Links,
        val rocket: Rocket
    ) : LaunchTypes()

    data class Carousel(
        val id: String,
        val items: List<RocketWithMission>
    ) : LaunchTypes()

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
        val launchDays: String
    ) : LaunchTypes()
}

@Keep
@Parcelize
data class Links(
    val missionImage: String,
    val articleLink: String?,
    val webcastLink: String?,
    val wikiLink: String?,
) : Parcelable

@Keep
@Parcelize
data class LinkType(
    val nameResId: Int,
    val link: String?,
    val onClick: () -> Unit
) : Parcelable

data class Rocket(
    val rocketNameAndType: String,
)

data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)