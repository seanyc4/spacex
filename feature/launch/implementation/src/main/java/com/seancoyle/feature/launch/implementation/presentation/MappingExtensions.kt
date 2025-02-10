package com.seancoyle.feature.launch.implementation.presentation

import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.api.domain.model.RocketWithMission
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.presentation.model.BottomSheetLinksUi
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchTypesUiModel
import com.seancoyle.feature.launch.implementation.presentation.model.LinksUi
import com.seancoyle.feature.launch.implementation.presentation.model.RocketUi
import com.seancoyle.feature.launch.implementation.presentation.model.RocketWithMissionUi
import dagger.Lazy

internal fun LaunchTypes.toUiModel(appStringResource: Lazy<AppStringResource>): LaunchTypesUiModel = when (this) {
    is LaunchTypes.CompanySummary -> LaunchTypesUiModel.CompanySummaryUi(
        id = id,
        summary = getSummary(appStringResource.get()),
        name = name,
        founder = founder,
        founded = founded.toString(),
        employees = employees,
        launchSites = launchSites.toString(),
        valuation = valuation
    )
    is LaunchTypes.Launch -> LaunchTypesUiModel.LaunchUi(
        id = id,
        launchDate = launchDate,
        launchYear = launchYear,
        launchStatus = launchStatus,
        links = links.toUiModel(),
        missionName = missionName,
        rocket = rocket.toUiModel(),
        launchDateStatus = launchDateStatus,
        launchDays = launchDays,
        launchDaysResId = launchDateStatus.getDateStringRes(),
        launchStatusIconResId = launchStatus.getDrawableRes()
    )
    is LaunchTypes.SectionTitle -> LaunchTypesUiModel.SectionTitleUi(
        id = id,
        title = title
    )

    is LaunchTypes.Grid -> LaunchTypesUiModel.GridUi(
        id = id,
        items = items.toUiModel()
    )

    is LaunchTypes.Carousel -> LaunchTypesUiModel.CarouselUi(
        id = id,
        items = items.map { it.toUiModel() }
    )
    else -> throw IllegalArgumentException("Unknown type")
}

fun Links.toUiModel() = LinksUi(
    missionImage = missionImage,
    articleLink = articleLink,
    webcastLink = webcastLink,
    wikiLink = wikiLink
)

fun Rocket.toUiModel() = RocketUi(
    rocketNameAndType = rocketNameAndType
)

fun RocketWithMission.toUiModel() = RocketWithMissionUi(
    links = links.toUiModel(),
    rocket = rocket.toUiModel()
)

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

