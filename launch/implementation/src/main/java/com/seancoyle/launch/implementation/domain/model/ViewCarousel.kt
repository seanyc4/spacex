package com.seancoyle.launch.implementation.domain.model

import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType

internal data class ViewCarousel(
    val id: String,
    val items: List<RocketWithMission>,
    override val type: Int
) : ViewType()

internal data class RocketWithMission(
    val links: Links,
    val rocket: Rocket
)