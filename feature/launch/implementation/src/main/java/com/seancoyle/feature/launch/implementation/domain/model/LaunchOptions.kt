package com.seancoyle.feature.launch.implementation.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LaunchOptions(
    val options: Options
)

@Serializable
internal data class Options(
    val limit: Int,
    val populate: List<Populate>,
    val sort: Sort
)

@Serializable
internal data class Populate(
    val path: String,
    val select: Select
)

@Serializable
internal data class Select(
    val name: Int,
    val type: Int
)

@Serializable
internal data class Sort(
    val flightNumber: String
)
