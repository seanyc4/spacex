package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

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
    override val type: Int
) : ViewType()

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