package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import androidx.paging.PagingData
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

internal class ObserveLaunchesUseCase @Inject constructor(
    private val launchRepository: LaunchRepository
) {
    operator fun invoke(launchQuery: LaunchQuery): Flow<PagingData<LaunchTypes.Launch>> {
        Timber.tag("LAUNCH_USE_CASE").d("ObserveLaunchesUseCase called with query: $launchQuery")
        return launchRepository.pager(launchQuery)
    }
}
