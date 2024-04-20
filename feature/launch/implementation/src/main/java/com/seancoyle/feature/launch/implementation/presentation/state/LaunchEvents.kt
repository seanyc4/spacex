package com.seancoyle.feature.launch.implementation.presentation.state

import com.seancoyle.core.ui.NotificationState

internal sealed interface LaunchEvents {
    data object GetLaunchesApiAndCacheEvent : LaunchEvents
    data object GetCompanyApiAndCacheEvent : LaunchEvents
    data object GetCompanyFromCacheEvent : LaunchEvents
    data object PaginateLaunchesCacheEvent : LaunchEvents
    data object GetNumLaunchesInCacheEvent : LaunchEvents
    data object CreateMergedLaunchesEvent : LaunchEvents


    class NotificationEvent(
        val notificationState: NotificationState
    ) : LaunchEvents

}