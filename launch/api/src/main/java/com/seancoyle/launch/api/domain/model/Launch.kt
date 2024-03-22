package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Keep
@Parcelize
data class Launch(
    val id: String,
    val launchDate: String,
    val launchDateLocalDateTime: LocalDateTime,
    val launchYear: String,
    val isLaunchSuccess: Int?,
    @DrawableRes val launchSuccessIcon: Int,
    val links: Links,
    val missionName: String,
    val rocket: Rocket,
    @StringRes val daysToFromTitle: Int,
    val launchDaysDifference: String,
    override val type: Int
) : Parcelable,
    ViewType()

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
    @StringRes val nameResId: Int,
    val link: String?,
    val onClick: () -> Unit
) : Parcelable

@Keep
@Parcelize
data class Rocket(
    val rocketNameAndType: String,
) : Parcelable