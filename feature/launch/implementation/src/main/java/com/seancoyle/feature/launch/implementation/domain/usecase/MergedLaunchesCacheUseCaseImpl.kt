package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.numberformatter.NumberFormatter
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.RocketWithMission
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.UUID
import javax.inject.Inject

internal class MergedLaunchesCacheUseCaseImpl @Inject constructor(
    private val getCompanyFromCacheUseCase: GetCompanyCacheUseCase,
    private val getLaunchesFromCacheUseCase: PaginateLaunchesCacheUseCase,
    private val numberFormatter: NumberFormatter
) : MergedLaunchesCacheUseCase {

    companion object {
        const val MAX_GRID_SIZE = 6
        const val MAX_CAROUSEL_SIZE = 20
        const val HEADER = "HEADER"
        const val CAROUSEL = "CAROUSEL"
        const val GRID = "GRID"
        const val LIST = "LIST"
    }

    override operator fun invoke(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<LaunchResult<List<LaunchTypes>, LocalError>> = flow {
        combine(
            getCompanyInfo().distinctUntilChanged(),
            getLaunches(
                launchYear = year,
                order = order,
                launchStatus = launchFilter,
                page = page
            ).distinctUntilChanged()
        ) { companyInfoResult, launchesResult ->

            // As Company and List<ViewType> are in the api module they cannot be smart casted
            val companyInfoData: Company? = (companyInfoResult as? LaunchResult.Success)?.data
            val launchesData: List<LaunchTypes>? = (launchesResult as? LaunchResult.Success)?.data

            when {
                companyInfoData != null && !launchesData.isNullOrEmpty() -> {
                    LaunchResult.Success(createMergedList(companyInfoData, launchesData))
                }

                companyInfoResult is LaunchResult.Error -> {
                    LaunchResult.Error(companyInfoResult.error)
                }

                launchesResult is LaunchResult.Error -> {
                    LaunchResult.Error(launchesResult.error)
                }

                launchesData.isNullOrEmpty() -> {
                    LaunchResult.Error(LocalError.CACHE_ERROR_NO_RESULTS)
                }

                else -> {
                    LaunchResult.Error(LocalError.CACHE_UNKNOWN_DATABASE_ERROR)
                }
            }

        }.collect { combinedResult ->
            emit(combinedResult)
        }
    }

    private fun getCompanyInfo(): Flow<LaunchResult<Company?, LocalError>> {
        return getCompanyFromCacheUseCase()
    }

    private fun getLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Flow<LaunchResult<List<LaunchTypes>?, LocalError>> {
        return getLaunchesFromCacheUseCase(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatus,
            page = page
        )
    }

    private fun createMergedList(
        company: Company,
        launches: List<LaunchTypes>,
    ): List<LaunchTypes> {
        val filteredLaunches = launches.filterIsInstance<LaunchTypes.Launch>()
        val mergedLaunches = mutableListOf<LaunchTypes>().apply {
            add(LaunchTypes.SectionTitle(UUID.randomUUID().toString(), HEADER))
            add(LaunchTypes.CompanySummary(UUID.randomUUID().toString(),
                summary = "",
                name = company.name,
                founder = company.founder,
                founded = company.founded,
                employees = numberFormatter.formatNumber(company.employees.toLong()),
                launchSites = company.launchSites,
                valuation = numberFormatter.formatNumber(company.valuation)
            ))
            add(LaunchTypes.SectionTitle(UUID.randomUUID().toString(), CAROUSEL))
            add(buildCarousel(filteredLaunches))
            add(LaunchTypes.SectionTitle(UUID.randomUUID().toString(), GRID))
            addAll(buildGrid(filteredLaunches))
            add(LaunchTypes.SectionTitle(UUID.randomUUID().toString(), LIST))
            addAll(launches)
        }
        return mergedLaunches
    }

    private fun buildGrid(launches: List<LaunchTypes.Launch>): List<LaunchTypes> {
        return launches.shuffled().take(MAX_GRID_SIZE).map { launchModel ->
            LaunchTypes.Grid(
                id = UUID.randomUUID().toString(),
                RocketWithMission(
                    links = launchModel.links,
                    rocket = launchModel.rocket
                )
            )
        }
    }

    private fun buildCarousel(launches: List<LaunchTypes.Launch>): LaunchTypes.Carousel {
        return LaunchTypes.Carousel(
            id = UUID.randomUUID().toString(),
            launches.shuffled().take(MAX_CAROUSEL_SIZE).map { launchModel ->
                RocketWithMission(
                    links = launchModel.links,
                    rocket = launchModel.rocket
                )
            }
        )
    }

}