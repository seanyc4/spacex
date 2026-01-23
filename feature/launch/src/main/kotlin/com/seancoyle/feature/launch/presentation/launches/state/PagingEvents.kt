package com.seancoyle.feature.launch.presentation.launches.state

sealed interface PagingEvents {
    data object Refresh : PagingEvents
    data object Retry : PagingEvents
}
