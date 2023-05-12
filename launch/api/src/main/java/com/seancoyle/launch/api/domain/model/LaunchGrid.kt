package com.seancoyle.launch.api.domain.model

data class LaunchGrid(
    val links: Links,
    val rocket: Rocket,
    override val type: Int
) : LaunchType()