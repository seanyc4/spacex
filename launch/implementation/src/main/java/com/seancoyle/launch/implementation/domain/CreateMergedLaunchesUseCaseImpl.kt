package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.presentation.util.StringResource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.LaunchCarousel
import com.seancoyle.launch.api.domain.model.LaunchModel
import com.seancoyle.launch.api.domain.model.LaunchType
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.RocketWithMission
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.R
import javax.inject.Inject

class CreateMergedLaunchesUseCaseImpl @Inject constructor(
    private val stringResource: StringResource
) : CreateMergedLaunchesUseCase {

    override operator fun invoke(
        companyInfo: CompanyInfo?,
        launches: List<LaunchModel>
    ): List<LaunchType> {
        val rocketLaunches = buildRockets(launches)
        val mergedLaunches = mutableListOf<LaunchType>().apply {
            add(SectionTitle(title = "COMPANY", type = LaunchType.TYPE_SECTION_TITLE))
            add(CompanySummary(summary = buildCompanySummary(companyInfo), type = LaunchType.TYPE_HEADER))
            add(SectionTitle(title = "ROCKETS", type = LaunchType.TYPE_SECTION_TITLE))
            add(LaunchCarousel(rocketLaunches, LaunchType.TYPE_CAROUSEL))
            add(SectionTitle(title = "GRID", type = LaunchType.TYPE_SECTION_TITLE))
            add(LaunchCarousel(rocketLaunches, LaunchType.TYPE_GRID))
            add(SectionTitle(title = "LAUNCHES", type = LaunchType.TYPE_SECTION_TITLE))
            addAll(launches)
        }
        return mergedLaunches
    }

    private fun buildRockets(launches: List<LaunchModel>): List<RocketWithMission> {
        val num = if (launches.size > 20) 20 else launches.size
        return launches.shuffled().take(num).map {
            RocketWithMission(
                Links(
                    articleLink = it.links.articleLink,
                    missionImage = it.links.missionImage,
                    webcastLink = it.links.webcastLink,
                    wikiLink = it.links.wikiLink,
                ),
                Rocket(
                    rocketNameAndType = it.rocket.rocketNameAndType
                )
            )
        }
    }

    private fun buildCompanySummary(companyInfo: CompanyInfo?) = String.format(
        stringResource.getString(R.string.company_info),
        companyInfo?.name,
        companyInfo?.founder,
        companyInfo?.founded,
        companyInfo?.employees,
        companyInfo?.launchSites,
        companyInfo?.valuation
    )
}