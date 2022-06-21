package com.seancoyle.launch_models.model.company

import com.seancoyle.launch_models.model.launch.LaunchType

data class CompanySummary(
    val summary: String,
    override val type: Int
) : LaunchType()