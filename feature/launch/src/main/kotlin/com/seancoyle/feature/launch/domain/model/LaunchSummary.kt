package com.seancoyle.feature.launch.domain.model

data class LaunchSummary(
    val id: String,
    val missionName: String,
    val net: String,
    val thumbnailUrl: String,
    val status: Status,
)
