package com.seancoyle.spacex.business.domain.model.company

import com.seancoyle.spacex.business.domain.model.launch.LaunchType

data class CompanySummary(
    val summary: String,
    override val type: Int
) : LaunchType()