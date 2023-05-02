package com.seancoyle.launch.api.model

data class SectionTitle(
    val title: String,
    override val type: Int
) : LaunchType()
