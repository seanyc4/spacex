package com.seancoyle.launch.implementation.domain.model


internal data class CompanySummary(
    val id: String,
    val summary: String,
    override val type: Int
) : ViewType()