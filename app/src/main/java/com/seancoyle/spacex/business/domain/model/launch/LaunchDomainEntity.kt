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
    LaunchType() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LaunchDomainEntity

        if (id != other.id) return false
        if (launchDate != other.launchDate) return false
        if (launchDateLocalDateTime != other.launchDateLocalDateTime) return false
        if (launchYear != other.launchYear) return false
        if (isLaunchSuccess != other.isLaunchSuccess) return false
        if (launchSuccessIcon != other.launchSuccessIcon) return false
        if (links != other.links) return false
        if (missionName != other.missionName) return false
        if (rocket != other.rocket) return false
        if (daysToFromTitle != other.daysToFromTitle) return false
        if (launchDaysDifference != other.launchDaysDifference) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + launchDate.hashCode()
        result = 31 * result + launchDateLocalDateTime.hashCode()
        result = 31 * result + launchYear.hashCode()
        result = 31 * result + isLaunchSuccess.hashCode()
        result = 31 * result + launchSuccessIcon
        result = 31 * result + links.hashCode()
        result = 31 * result + missionName.hashCode()
        result = 31 * result + rocket.hashCode()
        result = 31 * result + daysToFromTitle
        result = 31 * result + launchDaysDifference.hashCode()
        result = 31 * result + type
        return result
    }
}

@Parcelize
data class Links(
    val missionImage: String,
    val articleLink: String,
    val videoLink: String,
    val wikipedia: String,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Links

        if (missionImage != other.missionImage) return false
        if (articleLink != other.articleLink) return false
        if (videoLink != other.videoLink) return false
        if (wikipedia != other.wikipedia) return false

        return true
    }

    override fun hashCode(): Int {
        var result = missionImage.hashCode()
        result = 31 * result + articleLink.hashCode()
        result = 31 * result + videoLink.hashCode()
        result = 31 * result + wikipedia.hashCode()
        return result
    }
}

@Parcelize
data class Rocket(
    val rocketNameAndType: String,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rocket

        if (rocketNameAndType != other.rocketNameAndType) return false

        return true
    }

    override fun hashCode(): Int {
        return rocketNameAndType.hashCode()
    }
}

