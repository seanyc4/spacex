package com.seancoyle.feature.launch.presentation.launches.state

import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus

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

    /**
     * Event to select a launch in two-pane layouts.
     * Updates the detail pane without navigation.
     */
    data class SelectLaunchEvent(
        val launchId: String,
        val launchType: LaunchesType
    ) : LaunchesEvents

    /**
     * Event to clear the current selection in two-pane layouts.
     */
    data object ClearSelectionEvent : LaunchesEvents
}
