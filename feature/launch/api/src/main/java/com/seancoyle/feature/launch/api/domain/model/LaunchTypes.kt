package com.seancoyle.feature.launch.api.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

sealed interface LaunchTypes {
    data class CompanySummary(
        val id: String,
        val company: Company
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

    @Keep
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
        val launchDaysResId: Int,
        val launchStatusIconResId: Int
    ) : LaunchTypes
}

@Parcelize
@Keep
data class Links(
    @SerializedName("missionImage")
    val missionImage: String,
    @SerializedName("articleLink")
    val articleLink: String?,
    @SerializedName("webcastLink")
    val webcastLink: String?,
    @SerializedName("wikiLink")
    val wikiLink: String?,
) : Parcelable

@Parcelize
@Keep
data class BottomSheetLinks(
    @SerializedName("nameResId")
    val nameResId: Int,
    @SerializedName("link")
    val link: String?
) : Parcelable

data class Rocket(
    val rocketNameAndType: String,
)

data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)