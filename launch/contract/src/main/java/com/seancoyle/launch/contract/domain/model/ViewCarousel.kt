package com.seancoyle.launch.contract.domain.model

data class ViewCarousel(
    val items: List<RocketWithMission>,
    override val type: Int
) : ViewType()

data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)