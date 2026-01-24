package com.seancoyle.feature.launch.domain.usecase.launch

import androidx.paging.PagingData
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveUpcomingLaunchesUseCase @Inject constructor(
    private val launchesRepository: LaunchesRepository
) {
    operator fun invoke(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        return launchesRepository.upcomingPager(launchesQuery)
    }
}
