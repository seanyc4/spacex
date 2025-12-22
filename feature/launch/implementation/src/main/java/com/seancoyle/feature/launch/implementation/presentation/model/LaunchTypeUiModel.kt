package com.seancoyle.feature.launch.implementation.presentation.model

import android.os.Parcelable
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import kotlinx.parcelize.Parcelize

data class LaunchUi(
    val id: String,
    val missionName: String,
    val launchDate: String,
    val launchStatus: LaunchStatus,
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