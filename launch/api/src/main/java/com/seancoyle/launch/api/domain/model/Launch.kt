package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Launch(
    val id: Int,
    val launchDate: String,
    val launchDateLocalDateTime: LocalDateTime,
    val launchYear: String,
    val isLaunchSuccess: Int?,
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
    ViewType() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Launch

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
    val articleLink: String?,
    val webcastLink: String?,
    val wikiLink: String?,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Links

        if (missionImage != other.missionImage) return false
        if (articleLink != other.articleLink) return false
        if (webcastLink != other.webcastLink) return false
        if (wikiLink != other.wikiLink) return false

        return true
    }

    override fun hashCode(): Int {
        var result = missionImage.hashCode()
        result = 31 * result + articleLink.hashCode()
        result = 31 * result + webcastLink.hashCode()
        result = 31 * result + wikiLink.hashCode()
        return result
    }
}

@Parcelize
data class LinkType(
    @StringRes val nameResId: Int,
    val link: String?,
    val onClick: () -> Unit
) : Parcelable

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

