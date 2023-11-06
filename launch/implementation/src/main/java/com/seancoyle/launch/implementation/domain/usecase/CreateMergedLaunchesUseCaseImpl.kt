package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.StringResource
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.domain.model.Company
import com.seancoyle.launch.implementation.domain.model.CompanySummary
import com.seancoyle.launch.implementation.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.Links
import com.seancoyle.launch.implementation.domain.model.Rocket
import com.seancoyle.launch.implementation.domain.model.RocketWithMission
import com.seancoyle.launch.implementation.domain.model.SectionTitle
import com.seancoyle.launch.implementation.domain.model.ViewCarousel
import com.seancoyle.launch.implementation.domain.model.ViewGrid
import com.seancoyle.launch.implementation.domain.model.ViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID
import javax.inject.Inject

internal class CreateMergedLaunchesUseCaseImpl @Inject constructor(
    private val stringResource: StringResource,
    private val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    private val getLaunchesFromCacheUseCase: FilterLaunchItemsInCacheUseCase
) : CreateMergedLaunchesUseCase {

    override suspend operator fun invoke(
        year: String?,
        order: String,
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
        company: Company,
        launches: List<ViewType>,
    ): List<ViewType> {
        val mergedLaunches = mutableListOf<ViewType>().apply {
            launches as List<Launch>
            add(
                SectionTitle(
                    id = UUID.randomUUID().toString(),
                    title = "HEADER",
                    type = ViewType.TYPE_SECTION_TITLE
                )
            )
            add(
                CompanySummary(
                    id = UUID.randomUUID().toString(),
                    summary = buildCompanySummary(company),
                    type = ViewType.TYPE_HEADER
                )
            )
            add(
                SectionTitle(
                    id = UUID.randomUUID().toString(),
                    title = "CAROUSEL", type = ViewType.TYPE_SECTION_TITLE
                )
            )
            add(buildCarousel(launches))
            add(
                SectionTitle(
                    id = UUID.randomUUID().toString(),
                    title = "GRID", type = ViewType.TYPE_SECTION_TITLE
                )
            )
            addAll(buildGrid(launches))
            add(
                SectionTitle(
                    id = UUID.randomUUID().toString(),
                    title = "LIST", type = ViewType.TYPE_SECTION_TITLE
                )
            )
            addAll(launches)
        }
        return mergedLaunches
    }

    private fun buildGrid(launches: List<Launch>): List<ViewGrid> {
        return launches.shuffled().take(MAX_GRID_SIZE).map { launchModel ->
            ViewGrid(
                id = UUID.randomUUID().toString(),
                links = createLinks(launchModel.links),
                rocket = createRocket(launchModel.rocket),
                type = ViewType.TYPE_GRID
            )
        }
    }

    private fun buildCarousel(launches: List<Launch>): ViewCarousel {
        return ViewCarousel(
            id = UUID.randomUUID().toString(),
            launches.shuffled().take(MAX_CAROUSEL_SIZE).map { launchModel ->
                RocketWithMission(
                    createLinks(links = launchModel.links),
                    createRocket(rocket = launchModel.rocket)
                )
            },
            type = ViewType.TYPE_CAROUSEL
        )
    }

    private fun getCompanyInfo(): Flow<Company?> {
        return getCompanyInfoFromCacheUseCase()
    }

    private suspend fun getLaunches(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>?> {
        return getLaunchesFromCacheUseCase(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )

    }

    companion object {
        private const val MAX_GRID_SIZE = 6
        private const val MAX_CAROUSEL_SIZE = 20
    }

    private fun buildCompanySummary(company: Company?) = with(stringResource) {
        getString(R.string.company_info).format(
            company?.name,
            company?.founder,
            company?.founded,
            company?.employees,
            company?.launchSites,
            company?.valuation
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