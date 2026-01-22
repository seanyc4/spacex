package com.seancoyle.feature.launch.presentation.launches.state

import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus

sealed interface LaunchesEvent {
    data object DismissFilterBottomSheet : LaunchesEvent
    data object DisplayFilterBottomSheet : LaunchesEvent
    data object PullToRefresh : LaunchesEvent
    data object RetryFetch : LaunchesEvent
    data class TabSelected(val launchesType: LaunchesType) : LaunchesEvent
    data class UpdateFilterState(
        val query: String,
        val launchStatus: LaunchStatus,
    ) : LaunchesEvent
}
