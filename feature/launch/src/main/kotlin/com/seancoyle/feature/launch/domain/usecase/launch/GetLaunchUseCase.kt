package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.feature.launch.domain.model.LaunchesType
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import javax.inject.Inject

internal class GetLaunchUseCase @Inject constructor(
    private val launchesRepository: LaunchesRepository
) {
    suspend operator fun invoke(
        id: String,
        launchType: LaunchesType
    ) = launchesRepository.getLaunch(id, launchType)
}
