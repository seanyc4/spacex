package com.seancoyle.feature.launch.implementation.presentation.model

import android.os.Parcelable
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import kotlinx.parcelize.Parcelize

data class LaunchUi(
    val id: String,
    val launchDate: String,
    val launchYear: String,
    val launchStatus: LaunchStatus,
    val missionName: String,
    val launchDateStatus: LaunchDateStatus,
    val launchDays: String,
    val launchDaysResId: Int,
    val launchStatusIconResId: Int,
    val image: String
)

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