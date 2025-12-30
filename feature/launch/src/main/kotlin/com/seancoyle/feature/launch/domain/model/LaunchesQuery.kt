package com.seancoyle.feature.launch.domain.model

data class LaunchesQuery(
    val query: String = "",
    val status: LaunchStatus? = null,
    val launchesType: LaunchesType = LaunchesType.UPCOMING
)
