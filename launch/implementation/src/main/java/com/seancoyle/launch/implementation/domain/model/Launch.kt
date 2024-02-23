package com.seancoyle.launch.implementation.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Keep
@Parcelize
internal data class Launch(
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
internal data class Links(
    val missionImage: String,
    val articleLink: String?,
    val webcastLink: String?,
    val wikiLink: String?,
) : Parcelable

@Keep
@Parcelize
internal data class LinkType(
    @StringRes val nameResId: Int,
    val link: String?,
    val onClick: () -> Unit
) : Parcelable

@Keep
@Parcelize
internal data class Rocket(
    val rocketNameAndType: String,
) : Parcelable