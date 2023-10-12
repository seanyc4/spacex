package com.seancoyle.launch.api.domain.model


data class CompanySummary(
    val id: String,
    val summary: String,
    override val type: Int
) : ViewType()