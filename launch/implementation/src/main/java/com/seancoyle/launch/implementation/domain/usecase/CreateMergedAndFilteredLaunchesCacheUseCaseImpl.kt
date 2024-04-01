package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.core.domain.StringResource
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType
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

internal class CreateMergedAndFilteredLaunchesCacheUseCaseImpl @Inject constructor(
    private val stringResource: StringResource,
    private val getCompanyFromCacheUseCase: GetCompanyCacheUseCase,
    private val getLaunchesFromCacheUseCase: SortAndFilterLaunchesCacheUseCase
) : CreateMergedLaunchesCacheUseCase {

    companion object {
        private const val MAX_GRID_SIZE = 6
        private const val MAX_CAROUSEL_SIZE = 20
    }

    override operator fun invoke(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>, DataError>> = flow {
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
                companyInfoData != null && !launchesData.isNullOrEmpty() -> {
                    DataResult.Success(createMergedList(companyInfoData, launchesData))
                }

                companyInfoResult is DataResult.Error -> {
                    DataResult.Error(companyInfoResult.error)
                }

                launchesResult is DataResult.Error -> {
                    DataResult.Error(launchesResult.error)
                }

                launchesData.isNullOrEmpty() -> {
                    DataResult.Error(DataError.CACHE_ERROR_NO_RESULTS)
                }

                else -> {
                    DataResult.Error(DataError.UNKNOWN_DATABASE_ERROR)
                }
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
                    company = company,
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

    private fun getCompanyInfo(): Flow<DataResult<Company?, DataError>> {
        return getCompanyFromCacheUseCase()
    }

    private fun getLaunches(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>?, DataError>> {
        return getLaunchesFromCacheUseCase(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
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