package com.seancoyle.feature.launch.implementation.presentation.state

import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.Links

internal sealed interface LaunchEvents {
    data object CreateMergedLaunchesEvent : LaunchEvents
    data object DismissBottomSheetEvent : LaunchEvents
    data object DismissFilterDialogEvent : LaunchEvents
    data object DisplayFilterDialogEvent : LaunchEvents
    data object DismissNotificationEvent : LaunchEvents
    data object GetCompanyApiAndCacheEvent : LaunchEvents
    data object GetLaunchesApiAndCacheEvent : LaunchEvents
    data object NewSearchEvent : LaunchEvents
    data object PaginateLaunchesCacheEvent : LaunchEvents
    data object SwipeToRefreshEvent : LaunchEvents
    data class HandleLaunchClickEvent(val links: Links) : LaunchEvents
    data class LoadNextPageEvent(val page: Int) : LaunchEvents
    data class NotificationEvent(val notificationState: NotificationState) : LaunchEvents
    data class OpenLinkEvent(val url: String) : LaunchEvents
    data class SaveScrollPositionEvent(val position: Int) : LaunchEvents
    data class SetFilterStateEvent(
        val order: Order,
        val launchStatus: LaunchStatus,
        val launchYear: String
    ) : LaunchEvents
}