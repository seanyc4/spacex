package com.seancoyle.launch.api.domain.model


data class CompanySummary(
    val summary: String,
    override val type: Int
) : LaunchType()