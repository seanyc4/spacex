package com.seancoyle.feature.launch.implementation.presentation.state

import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus

internal sealed interface LaunchEvents {
    data object DismissFilterDialogEvent : LaunchEvents
    data object DisplayFilterDialogEvent : LaunchEvents
    data object DismissNotificationEvent : LaunchEvents
    data object NewSearchEvent : LaunchEvents
    data object SwipeToRefreshEvent : LaunchEvents
    data class NotificationEvent(val notificationState: NotificationState) : LaunchEvents
    data class UpdateScrollPositionEvent(val position: Int) : LaunchEvents
    data class UpdateFilterStateEvent(
        val order: Order,
        val launchStatus: LaunchStatus,
        val launchYear: String
    ) : LaunchEvents
}
