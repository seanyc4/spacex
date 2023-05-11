package com.seancoyle.launch.api.domain.model

data class LaunchCarousel(
    val items: List<RocketWithMission>,
    override val type: Int
) : LaunchType()

data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)