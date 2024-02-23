package com.seancoyle.launch.implementation.domain.model

internal data class ViewGrid(
    val id: String,
    val links: Links,
    val rocket: Rocket,
    override val type: Int
) : ViewType()