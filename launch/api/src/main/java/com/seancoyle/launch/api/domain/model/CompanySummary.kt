package com.seancoyle.launch.api.domain.model

import androidx.annotation.Keep

@Keep
data class CompanySummary(
    val summary: String,
    override val type: Int
) : ViewType()