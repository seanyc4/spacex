package com.seancoyle.feature.launch.presentation.model

import com.seancoyle.feature.launch.domain.model.LaunchStatus

data class LaunchUi(
    val id: String,
    val missionName: String,
    val launchDate: String,
    val launchStatus: LaunchStatus,
    val launchDays: String,
    val image: String
)
