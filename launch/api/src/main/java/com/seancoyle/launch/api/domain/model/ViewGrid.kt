package com.seancoyle.launch.api.domain.model

data class ViewGrid(
    val id: String,
    val links: Links,
    val rocket: Rocket,
    override val type: Int
) : ViewType()