package com.seancoyle.feature.launch.presentation.launches.model

import com.seancoyle.feature.launch.domain.model.LaunchStatus

data class LaunchesUi(
    val id: String,
    val missionName: String,
    val launchDate: String,
    val status: LaunchStatus,
    val image: String
)
