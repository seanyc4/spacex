package com.seancoyle.launch.api.domain.model

data class SectionTitle(
    val id: String,
    val title: String,
    override val type: Int
) : ViewType()
