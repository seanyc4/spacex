package com.seancoyle.feature.launch.presentation.state

import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchStatus

internal sealed interface LaunchesEvents {
    data object DismissFilterDialogEvent : LaunchesEvents
    data object DisplayFilterDialogEvent : LaunchesEvents
    data object NewSearchEvent : LaunchesEvents
    data object PullToRefreshEvent : LaunchesEvents
    data object RetryFetchEvent : LaunchesEvents
    data class UpdateFilterStateEvent(
        val query: String,
        val order: Order,
        val launchStatus: LaunchStatus,
    ) : LaunchesEvents
}
