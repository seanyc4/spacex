package com.seancoyle.feature.launch.presentation.launches.state

import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.model.LaunchesType

sealed interface LaunchesEvents {
    data object DismissFilterDialogEvent : LaunchesEvents
    data object DisplayFilterDialogEvent : LaunchesEvents
    data object NewSearchEvent : LaunchesEvents
    data object PullToRefreshEvent : LaunchesEvents
    data object RetryFetchEvent : LaunchesEvents
    data class UpdateFilterStateEvent(
        val query: String,
        val launchStatus: LaunchStatus,
    ) : LaunchesEvents
    data class TabSelectedEvent(val launchesType: LaunchesType) : LaunchesEvents
}
