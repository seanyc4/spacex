package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import androidx.paging.PagingData
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveLaunchesUseCase @Inject constructor(
    private val launchRepository: LaunchRepository
) {
    operator fun invoke(): Flow<PagingData<LaunchTypes.Launch>> {
        return launchRepository.pager()
    }
}
