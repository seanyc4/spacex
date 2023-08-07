package com.seancoyle.launch.api.domain.model

import androidx.annotation.Keep

@Keep
data class ViewCarousel(
    val items: List<RocketWithMission>,
    override val type: Int
) : ViewType()

@Keep
data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)