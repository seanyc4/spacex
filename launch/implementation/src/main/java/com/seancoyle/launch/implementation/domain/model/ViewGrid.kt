package com.seancoyle.launch.implementation.domain.model

import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType

internal data class ViewGrid(
    val id: String,
    val links: Links,
    val rocket: Rocket,
    override val type: Int
) : ViewType()