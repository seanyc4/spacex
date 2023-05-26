package com.seancoyle.launch.contract.domain.model


data class CompanySummary(
    val summary: String,
    override val type: Int
) : ViewType()