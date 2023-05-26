package com.seancoyle.launch.contract.domain.model

data class ViewGrid(
    val links: Links,
    val rocket: Rocket,
    override val type: Int
) : ViewType()