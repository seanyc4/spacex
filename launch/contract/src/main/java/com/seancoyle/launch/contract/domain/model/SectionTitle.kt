package com.seancoyle.launch.contract.domain.model

data class SectionTitle(
    val title: String,
    override val type: Int
) : ViewType()
