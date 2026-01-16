package com.seancoyle.feature.launch.presentation.launches.state

import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus

sealed interface LaunchesEvents {
    data object DismissFilterBottomSheetEvent : LaunchesEvents
    data object DisplayFilterBottomSheetEvent : LaunchesEvents
    data object PullToRefreshEvent : LaunchesEvents
    data object RetryFetchEvent : LaunchesEvents
    data class UpdateFilterStateEvent(
        val query: String,
        val launchStatus: LaunchStatus,
    ) : LaunchesEvents
    data class TabSelectedEvent(val launchesType: LaunchesType) : LaunchesEvents
}
