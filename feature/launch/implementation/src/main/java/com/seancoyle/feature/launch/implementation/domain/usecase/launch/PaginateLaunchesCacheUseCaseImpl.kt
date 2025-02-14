package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class PaginateLaunchesCacheUseCaseImpl @Inject constructor(
    private val launchRepository: LaunchRepository
) : PaginateLaunchesCacheUseCase {

    override operator fun invoke(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Flow<LaunchResult<List<LaunchTypes>, DataSourceError>> = flow {
        emit(
            launchRepository.paginateCache(
                launchYear = launchYear,
                order = order,
                launchStatus = launchStatus,
                page = page
            )
        )
    }
}