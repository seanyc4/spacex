package com.seancoyle.launch.implementation.domain.model

import com.seancoyle.launch.api.domain.model.ViewType

internal data class CompanySummary(
    val id: String,
    val summary: String,
    override val type: Int
) : ViewType()