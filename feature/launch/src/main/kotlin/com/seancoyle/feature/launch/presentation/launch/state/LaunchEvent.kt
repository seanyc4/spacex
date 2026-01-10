package com.seancoyle.feature.launch.presentation.launch.state

sealed interface LaunchEvent {
    data object RetryFetch : LaunchEvent
    data object PullToRefreshEvent : LaunchEvent
}
