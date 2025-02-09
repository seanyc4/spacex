package com.seancoyle.feature.launch.implementation.presentation

import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.presentation.model.BottomSheetLinksUi
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi

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

fun LaunchTypes.CompanySummary.getSummary(appStringResource: AppStringResource): String {
    val summary = appStringResource.getString(
        R.string.company_info,
        arrayOf(
            this.name,
            this.founder,
            this.founded,
            this.employees,
            this.launchSites,
            this.valuation
        )
    )
    return summary
}

fun LinksUi?.getLinks() = listOfNotNull(
    this?.articleLink?.let { BottomSheetLinksUi(R.string.article, it) },
    this?.webcastLink?.let { BottomSheetLinksUi(R.string.webcast, it) },
    this?.wikiLink?.let { BottomSheetLinksUi(R.string.wikipedia, it) }
)