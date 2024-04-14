package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.RocketWithMission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

internal class MergedLaunchesCacheUseCaseImpl @Inject constructor(
    private val getCompanyFromCacheUseCase: GetCompanyCacheUseCase,
    private val getLaunchesFromCacheUseCase: SortAndFilterLaunchesCacheUseCase
) : MergedLaunchesCacheUseCase {

    companion object {
        private const val MAX_GRID_SIZE = 6
        private const val MAX_CAROUSEL_SIZE = 20
        private const val HEADER = "HEADER"
        private const val CAROUSEL = "CAROUSEL"
        private const val GRID = "GRID"
        private const val LIST = "LIST"
    }

    override operator fun invoke(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<Result<List<LaunchTypes>, DataError>> = flow {
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
            val companyInfoData: Company? = (companyInfoResult as? Result.Success)?.data
            val launchesData: List<LaunchTypes>? = (launchesResult as? Result.Success)?.data

            when {
                companyInfoData != null && !launchesData.isNullOrEmpty() -> {
                    Result.Success(createMergedList(companyInfoData, launchesData))
                }

                companyInfoResult is Result.Error -> {
                    Result.Error(companyInfoResult.error)
                }

                launchesResult is Result.Error -> {
                    Result.Error(launchesResult.error)
                }

                launchesData.isNullOrEmpty() -> {
                    Result.Error(DataError.CACHE_ERROR_NO_RESULTS)
                }

                else -> {
                    Result.Error(DataError.CACHE_UNKNOWN_DATABASE_ERROR)
                }
            }

        }.collect { combinedResult ->
            emit(combinedResult)
        }
    }

    private fun getCompanyInfo(): Flow<Result<Company?, DataError>> {
        return getCompanyFromCacheUseCase()
    }

    private fun getLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Flow<Result<List<LaunchTypes>?, DataError>> {
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
            add(LaunchTypes.CompanySummary(UUID.randomUUID().toString(), company))
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
                links = launchModel.links,
                rocket = launchModel.rocket
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