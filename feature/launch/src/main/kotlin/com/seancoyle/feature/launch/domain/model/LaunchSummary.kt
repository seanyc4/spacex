package com.seancoyle.feature.launch.domain.model

data class LaunchSummary(
    val id: String,
    val missionName: String,
    val net: String,
    val imageUrl: String,
    val status: Status,
)
