package com.seancoyle.spacex.business.domain.model.launch

data class SectionTitle(
    val title: String,
    override val type: Int
) : LaunchType()
