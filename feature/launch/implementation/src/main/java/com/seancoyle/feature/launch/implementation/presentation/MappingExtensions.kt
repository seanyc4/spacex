package com.seancoyle.feature.launch.implementation.presentation

import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.presentation.model.BottomSheetLinksUi
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchTypesUiModel
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi

internal fun LaunchTypes.toUiModel(appStringResource: AppStringResource): LaunchTypesUiModel =
    when (this) {
        is LaunchTypes.Launch -> LaunchTypesUiModel.LaunchUi(
            id = id,
            launchDate = launchDate!!,
            launchYear = "",
            launchStatus = launchStatus,
            missionName = "",
            launchDateStatus = LaunchDateStatus.FUTURE,
            launchDays = "",
            launchDaysResId = LaunchDateStatus.FUTURE.getDateStringRes(),
            launchStatusIconResId = launchStatus.getDrawableRes(),
            image = image?.thumbnailUrl.orEmpty()
        )
    }

fun LaunchStatus.getDrawableRes(): Int = when (this) {
    LaunchStatus.SUCCESS -> R.drawable.ic_launch_success
    LaunchStatus.FAILED -> R.drawable.ic_launch_fail
    LaunchStatus.UNKNOWN -> R.drawable.ic_launch_unknown
    LaunchStatus.ALL -> throw IllegalArgumentException("LaunchStatus.ALL is not supported here")
}

fun LaunchDateStatus.getDateStringRes(): Int = when (this) {
    LaunchDateStatus.PAST -> R.string.days_since_now
    LaunchDateStatus.FUTURE -> R.string.days_from_now
}

fun LinksUi?.getLinks() = listOfNotNull(
    this?.articleLink?.let { BottomSheetLinksUi(R.string.article, it) },
    this?.webcastLink?.let { BottomSheetLinksUi(R.string.webcast, it) },
    this?.wikiLink?.let { BottomSheetLinksUi(R.string.wikipedia, it) }
)
