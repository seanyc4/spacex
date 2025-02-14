package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetNumLaunchesCacheUseCaseImpl @Inject constructor(
    private val launchRepository: LaunchRepository
) : GetNumLaunchesCacheUseCase {

    override operator fun invoke(): Flow<LaunchResult<Int?, DataSourceError>> = flow {
        emit(launchRepository.getTotalEntriesCache())
    }
}