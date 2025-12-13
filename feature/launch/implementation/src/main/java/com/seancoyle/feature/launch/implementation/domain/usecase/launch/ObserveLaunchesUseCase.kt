package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveLaunchesUseCase @Inject constructor(
    private val launchRepository: LaunchRepository
) {
    operator fun invoke(): Flow<LaunchResult<List<LaunchTypes.Launch>, LocalError>> {
        return launchRepository.observeAll()
    }
}
