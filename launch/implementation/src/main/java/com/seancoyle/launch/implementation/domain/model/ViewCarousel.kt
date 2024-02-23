package com.seancoyle.launch.implementation.domain.model


internal data class ViewCarousel(
    val id: String,
    val items: List<RocketWithMission>,
    override val type: Int
) : ViewType()

internal data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)