package com.seancoyle.feature.launch.domain.model

internal data class DetailedLaunchesResult(
    val summaries: List<LaunchSummary>,
    val details: List<Launch>
)
