package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.presentation.util.StringResource
import com.seancoyle.launch.contract.domain.model.CompanyInfo
import com.seancoyle.launch.contract.domain.model.CompanySummary
import com.seancoyle.launch.contract.domain.model.Links
import com.seancoyle.launch.contract.domain.model.Rocket
import com.seancoyle.launch.contract.domain.model.RocketWithMission
import com.seancoyle.launch.contract.domain.model.SectionTitle
import com.seancoyle.launch.contract.domain.model.ViewCarousel
import com.seancoyle.launch.contract.domain.model.ViewGrid
import com.seancoyle.launch.contract.domain.model.ViewModel
import com.seancoyle.launch.contract.domain.model.ViewType
import com.seancoyle.launch.contract.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.R
import javax.inject.Inject

class CreateMergedLaunchesUseCaseImpl @Inject constructor(
    private val stringResource: StringResource
) : CreateMergedLaunchesUseCase {

    companion object {
        private const val MAX_GRID_SIZE = 6
        private const val MAX_CAROUSEL_SIZE = 20
    }

    override operator fun invoke(
        companyInfo: CompanyInfo?,
        launches: List<ViewModel>
    ): List<ViewType> {
        val mergedLaunches = mutableListOf<ViewType>().apply {
            add(SectionTitle(title = "HEADER", type = ViewType.TYPE_SECTION_TITLE))
            add(CompanySummary(summary = buildCompanySummary(companyInfo), type = ViewType.TYPE_HEADER))
            add(SectionTitle(title = "CAROUSEL", type = ViewType.TYPE_SECTION_TITLE))
            add(buildCarousel(launches))
            add(SectionTitle(title = "GRID", type = ViewType.TYPE_SECTION_TITLE))
            addAll(buildGrid(launches))
            add(SectionTitle(title = "LIST", type = ViewType.TYPE_SECTION_TITLE))
            addAll(launches)
        }
        return mergedLaunches
    }

    private fun buildGrid(launches: List<ViewModel>): List<ViewGrid> {
        return launches.shuffled().take(MAX_GRID_SIZE).map { launchModel ->
            ViewGrid(
                links = createLinks(launchModel.links),
                rocket = createRocket(launchModel.rocket),
                type = ViewType.TYPE_GRID
            )
        }
    }

    private fun buildCarousel(launches: List<ViewModel>): ViewCarousel {
        return ViewCarousel(
            launches.shuffled().take(MAX_CAROUSEL_SIZE).map { launchModel ->
                RocketWithMission(
                    createLinks(links = launchModel.links),
                    createRocket(rocket = launchModel.rocket)
                )
            },
            type = ViewType.TYPE_CAROUSEL
        )
    }

    private fun buildCompanySummary(companyInfo: CompanyInfo?) = with(stringResource) {
        getString(R.string.company_info).format(
            companyInfo?.name,
            companyInfo?.founder,
            companyInfo?.founded,
            companyInfo?.employees,
            companyInfo?.launchSites,
            companyInfo?.valuation
        )
    }

    private fun createLinks(links: Links) = Links(
        articleLink = links.articleLink,
        missionImage = links.missionImage,
        webcastLink = links.webcastLink,
        wikiLink = links.wikiLink
    )

    private fun createRocket(rocket: Rocket) = Rocket(rocket.rocketNameAndType)

}