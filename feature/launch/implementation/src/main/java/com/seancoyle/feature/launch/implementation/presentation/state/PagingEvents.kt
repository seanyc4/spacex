package com.seancoyle.feature.launch.implementation.presentation.state

sealed interface PagingEvents {
    data object Refresh : PagingEvents
    data object Retry : PagingEvents
}
