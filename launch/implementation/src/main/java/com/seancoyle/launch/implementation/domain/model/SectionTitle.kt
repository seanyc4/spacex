package com.seancoyle.launch.implementation.domain.model

internal data class SectionTitle(
    val id: String,
    val title: String,
    override val type: Int
) : ViewType()
