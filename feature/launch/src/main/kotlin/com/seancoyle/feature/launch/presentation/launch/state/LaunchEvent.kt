package com.seancoyle.feature.launch.presentation.launch.state

sealed interface LaunchEvent {
    data object Retry : LaunchEvent
    data object PullToRefresh : LaunchEvent
}
