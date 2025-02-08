package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
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
    ): Flow<Result<List<LaunchTypes>, DataError>> = flow {
        emit(
            launchRepository.paginateLaunches(
                launchYear = launchYear,
                order = order,
                launchStatus = launchStatus,
                page = page
            )
        )
    }
}