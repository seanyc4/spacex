package com.seancoyle.feature.launch.presentation.launch

sealed interface LaunchEvent {
    data object RetryFetch : LaunchEvent
}
