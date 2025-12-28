package com.seancoyle.feature.launch.presentation.state

sealed interface PagingEvents {
    data object Refresh : PagingEvents
    data object Retry : PagingEvents
}
