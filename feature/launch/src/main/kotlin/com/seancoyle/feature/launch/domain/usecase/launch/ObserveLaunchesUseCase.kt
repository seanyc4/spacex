package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import androidx.paging.filter
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

internal class ObserveLaunchesUseCase @Inject constructor(
    private val launchesRepository: LaunchesRepository
) {
    operator fun invoke(launchesQuery: LaunchesQuery): Flow<PagingData<Launch>> {
        Timber.tag("LAUNCH_USE_CASE").d("ObserveLaunchesUseCase called with query: $launchesQuery")
        val result = launchesRepository.pager(launchesQuery)
        return if (launchesQuery.status != null) {
            result.map { pagingData ->
                pagingData.filter { launch ->
                    launch.status == launchesQuery.status
                }
            }
        } else {
            result
        }
    }
}
