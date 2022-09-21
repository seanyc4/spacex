package com.seancoyle.launch_models.model.launch

data class SectionTitle(
    val title: String,
    override val type: Int
) : LaunchType()
