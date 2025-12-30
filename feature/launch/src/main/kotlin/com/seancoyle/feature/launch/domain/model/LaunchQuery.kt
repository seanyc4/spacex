package com.seancoyle.feature.launch.domain.model

data class LaunchQuery(
    val query: String = "",
    val status: LaunchStatus? = null,
    val launchType: LaunchType = LaunchType.UPCOMING
)
