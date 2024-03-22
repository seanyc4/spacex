package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.CACHE_ERROR_UNKNOWN
import com.seancoyle.core.data.DataResult
import com.seancoyle.core.domain.StringResource
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.domain.model.CompanySummary
import com.seancoyle.launch.implementation.domain.model.RocketWithMission
import com.seancoyle.launch.implementation.domain.model.SectionTitle
import com.seancoyle.launch.implementation.domain.model.ViewCarousel
import com.seancoyle.launch.implementation.domain.model.ViewGrid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
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
    ): Flow<DataResult<List<ViewType>>> = flow {
        combine(
            getCompanyInfo().distinctUntilChanged(),
            getLaunches(
                year = year,
                order = order,
                launchFilter = launchFilter,
                page = page
            ).distinctUntilChanged()
        ) { companyInfoResult, launchesResult ->

            // As Company and List<ViewType> are in the api module they cannot be smart casted
            val companyInfoData: Company? = (companyInfoResult as? DataResult.Success)?.data
            val launchesData: List<ViewType>? = (launchesResult as? DataResult.Success)?.data

            when {
                companyInfoData != null && !launchesData.isNullOrEmpty() ->
                    DataResult.Success(createMergedList(companyInfoData, launchesData))

                companyInfoResult is DataResult.Error ->
                    DataResult.Error(companyInfoResult.exception)

                launchesResult is DataResult.Error ->
                    DataResult.Error(launchesResult.exception)

                else -> DataResult.Error(CACHE_ERROR_UNKNOWN)
            }

        }.collect { combinedResult ->
                emit(combinedResult)
            }
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

    private fun getCompanyInfo(): Flow<DataResult<Company?>> {
        return getCompanyInfoFromCacheUseCase()
    }

    private suspend fun getLaunches(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>> {
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