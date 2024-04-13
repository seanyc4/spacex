package com.seancoyle.launch.api.domain.model

import com.seancoyle.core.domain.Order

data class LaunchPrefs(
    val order: Order,
    val launchStatus: LaunchStatus,
    val launchYear: String
)

enum class LaunchStatus {
    SUCCESS, FAILED, UNKNOWN, ALL
}

enum class LaunchDateStatus {
    PAST, FUTURE
}