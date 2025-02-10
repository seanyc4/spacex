package com.seancoyle.feature.launch.implementation.presentation.model

import android.os.Parcelable
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import kotlinx.parcelize.Parcelize

internal sealed interface LaunchTypesUiModel {
    data class CompanySummaryUi(
        val id: String,
        val summary: String,
        val name: String,
        val founder: String,
        val founded: String,
        val employees: String,
        val launchSites: String,
        val valuation: String
    ) : LaunchTypesUiModel

    data class SectionTitleUi(
        val id: String,
        val title: String
    ) : LaunchTypesUiModel

    data class GridUi(
        val id: String,
        val items: RocketWithMissionUi
    ) : LaunchTypesUiModel

    data class CarouselUi(
        val id: String,
        val items: List<RocketWithMissionUi>
    ) : LaunchTypesUiModel

    data class LaunchUi(
        val id: String,
        val launchDate: String,
        val launchYear: String,
        val launchStatus: LaunchStatus,
        val links: LinksUi,
        val missionName: String,
        val rocket: RocketUi,
        val launchDateStatus: LaunchDateStatus,
        val launchDays: String,
        val launchDaysResId: Int,
        val launchStatusIconResId: Int
    ) : LaunchTypesUiModel
}

@Parcelize
data class LinksUi(
    val missionImage: String,
    val articleLink: String?,
    val webcastLink: String?,
    val wikiLink: String?
) : Parcelable

@Parcelize
data class BottomSheetLinksUi(
    val nameResId: Int,
    val link: String?
) : Parcelable

data class RocketUi(
    val rocketNameAndType: String
)

data class RocketWithMissionUi(
    val links: LinksUi,
    val rocket: RocketUi
)