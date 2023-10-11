package com.seancoyle.launch.implementation.presentation

import com.seancoyle.launch.api.domain.model.ViewType

data class LaunchState(
    val mergedLaunches: List<ViewType>? = emptyList(),
    val scrollPosition: Int? = 0,
    val isLoading: Boolean = false
)

