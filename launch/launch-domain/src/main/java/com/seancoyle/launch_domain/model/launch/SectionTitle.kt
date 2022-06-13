package com.seancoyle.launch_domain.model.launch

data class SectionTitle(
    val title: String,
    override val type: Int
) : LaunchType()
