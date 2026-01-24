package com.seancoyle.feature.launch.domain.model

import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus

data class LaunchesQuery(
    val query: String = "",
    val status: LaunchStatus? = null
)
