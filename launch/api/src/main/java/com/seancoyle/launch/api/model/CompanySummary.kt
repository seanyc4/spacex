package com.seancoyle.launch.api.model


data class CompanySummary(
    val summary: String,
    override val type: Int
) : LaunchType()