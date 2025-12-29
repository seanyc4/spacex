package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import androidx.paging.filter
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

internal class ObserveLaunchesUseCase @Inject constructor(
    private val launchRepository: LaunchRepository
) {
    operator fun invoke(launchQuery: LaunchQuery): Flow<PagingData<Launch>> {
        Timber.tag("LAUNCH_USE_CASE").d("ObserveLaunchesUseCase called with query: $launchQuery")
        val result = launchRepository.pager(launchQuery)
        return if (launchQuery.status != null) {
            result.map { pagingData ->
                pagingData.filter { launch ->
                    launch.status == launchQuery.status
                }
            }
        } else {
            result
        }
    }
}
