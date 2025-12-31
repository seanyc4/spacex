package com.seancoyle.feature.launch.domain.model

import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.LaunchStatus

data class LaunchesQuery(
    val query: String = "",
    val status: LaunchStatus? = null,
    val launchesType: LaunchesType = LaunchesType.UPCOMING
)
