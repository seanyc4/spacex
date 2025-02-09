package com.seancoyle.feature.launch.implementation.presentation

import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.feature.launch.api.domain.model.BottomSheetLinks
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.R

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

fun Links?.getLinks() = listOfNotNull(
    this?.articleLink?.let { BottomSheetLinks(R.string.article, it) },
    this?.webcastLink?.let { BottomSheetLinks(R.string.webcast, it) },
    this?.wikiLink?.let { BottomSheetLinks(R.string.wikipedia, it) }
)