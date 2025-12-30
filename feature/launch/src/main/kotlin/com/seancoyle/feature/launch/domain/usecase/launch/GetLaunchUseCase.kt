package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import javax.inject.Inject

internal class GetLaunchUseCase @Inject constructor(
    private val launchesRepository: LaunchesRepository
) {
    suspend operator fun invoke(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, DataError.RemoteError> {
        return launchesRepository.getLaunch(id, launchType)
    }
}
