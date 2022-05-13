package com.seancoyle.spacex.business.domain.model.launch

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class LaunchDomainEntity(
    val id: Int,
    val launchDate: String,
    val launchDateLocalDateTime: LocalDateTime,
    val launchYear: String,
    val isLaunchSuccess: Boolean,
    @DrawableRes
    val launchSuccessIcon: Int,
    val links: Links,
    val missionName: String,
    val rocket: Rocket,
    @StringRes
    val daysToFromTitle: Int,
    val launchDaysDifference: String,
    override val type: Int
) : Parcelable,
    LaunchType()

@Parcelize
data class Links(
    val missionImage: String,
    val articleLink: String,
    val videoLink: String,
    val wikipedia: String,
) : Parcelable

@Parcelize
data class Rocket(
    val rocketNameAndType: String,
) : Parcelable

