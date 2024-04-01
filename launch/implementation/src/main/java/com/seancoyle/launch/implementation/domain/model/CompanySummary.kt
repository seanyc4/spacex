package com.seancoyle.launch.implementation.domain.model

import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.ViewType

internal data class CompanySummary(
    val id: String,
    val company: Company,
    override val type: Int
) : ViewType()