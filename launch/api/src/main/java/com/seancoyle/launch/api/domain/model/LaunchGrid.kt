package com.seancoyle.launch.api.domain.model

data class LaunchGrid(
    val items: List<LaunchModel>,
    override val type: Int
) : LaunchType()