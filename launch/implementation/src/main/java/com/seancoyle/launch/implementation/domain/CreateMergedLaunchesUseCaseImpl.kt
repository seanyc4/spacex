package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.domain.StringResource
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.CompanySummary
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.RocketWithMission
import com.seancoyle.launch.api.domain.model.SectionTitle
import com.seancoyle.launch.api.domain.model.ViewCarousel
import com.seancoyle.launch.api.domain.model.ViewGrid
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.api.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.implementation.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class CreateMergedLaunchesUseCaseImpl @Inject constructor(
    private val stringResource: StringResource,
    private val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    private val getLaunchesFromCacheUseCase: FilterLaunchItemsInCacheUseCase
) : CreateMergedLaunchesUseCase {

    override operator fun invoke(
        year: String?,
        order: String?,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>> {
        val result: Flow<List<ViewType>> = combine(
            getCompanyInfo().distinctUntilChanged(),
            getLaunches(
                year = year,
                order = order,
                launchFilter = launchFilter,
                page = page
            ).distinctUntilChanged()
        ) { companyInfoFlow, launchesFlow ->
            if (companyInfoFlow != null && !launchesFlow.isNullOrEmpty()) {
                createMergedList(companyInfoFlow, launchesFlow)
            } else {
                emptyList()
            }
        }
        return result
    }

    private fun createMergedList(
        companyInfo: CompanyInfo,
        launches: List<Launch>,
    ): List<ViewType> {
        val mergedLaunches = mutableListOf<ViewType>().apply {
            add(SectionTitle(title = "HEADER", type = ViewType.TYPE_SECTION_TITLE))
            add(
                CompanySummary(
                    summary = buildCompanySummary(companyInfo),
                    type = ViewType.TYPE_HEADER
                )
            )
            add(SectionTitle(title = "CAROUSEL", type = ViewType.TYPE_SECTION_TITLE))
            add(buildCarousel(launches))
            add(SectionTitle(title = "GRID", type = ViewType.TYPE_SECTION_TITLE))
            addAll(buildGrid(launches))
            add(SectionTitle(title = "LIST", type = ViewType.TYPE_SECTION_TITLE))
            addAll(launches)
        }
        return mergedLaunches
    }

    private fun buildGrid(launches: List<Launch>): List<ViewGrid> {
        return launches.shuffled().take(MAX_GRID_SIZE).map { launchModel ->
            ViewGrid(
                links = createLinks(launchModel.links),
                rocket = createRocket(launchModel.rocket),
                type = ViewType.TYPE_GRID
            )
        }
    }

    private fun buildCarousel(launches: List<Launch>): ViewCarousel {
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

    private fun getCompanyInfo(): Flow<CompanyInfo?> {
        return getCompanyInfoFromCacheUseCase()
    }

    private fun getLaunches(
        year: String?,
        order: String?,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<Launch>?> {
        return getLaunchesFromCacheUseCase(
            year = year,
            order = order ?: ORDER_ASC,
            launchFilter = launchFilter,
            page = page
        )
    }

    companion object {
        private const val MAX_GRID_SIZE = 6
        private const val MAX_CAROUSEL_SIZE = 20
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