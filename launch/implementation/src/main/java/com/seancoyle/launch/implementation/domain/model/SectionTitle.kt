package com.seancoyle.launch.implementation.domain.model

import com.seancoyle.launch.api.domain.model.ViewType

internal data class SectionTitle(
    val id: String,
    val title: String,
    override val type: Int
) : ViewType()
