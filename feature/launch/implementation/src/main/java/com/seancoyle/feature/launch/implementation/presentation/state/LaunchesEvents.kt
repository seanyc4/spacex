package com.seancoyle.feature.launch.implementation.presentation.state

import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus

internal sealed interface LaunchesEvents {
    data object DismissFilterDialogEvent : LaunchesEvents
    data object DisplayFilterDialogEvent : LaunchesEvents
    data object DismissNotificationEvent : LaunchesEvents
    data object NewSearchEvent : LaunchesEvents
    data object SwipeToRefreshEvent : LaunchesEvents
    data class NotificationEvent(val notificationState: NotificationState) : LaunchesEvents
    data class UpdateScrollPositionEvent(val position: Int) : LaunchesEvents
    data class UpdateFilterStateEvent(
        val order: Order,
        val launchStatus: LaunchStatus,
        val launchYear: String
    ) : LaunchesEvents
}
