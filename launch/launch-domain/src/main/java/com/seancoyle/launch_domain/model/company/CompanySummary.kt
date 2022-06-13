package com.seancoyle.launch_domain.model.company

import com.seancoyle.launch_domain.model.launch.LaunchType

data class CompanySummary(
    val summary: String,
    override val type: Int
) : LaunchType()