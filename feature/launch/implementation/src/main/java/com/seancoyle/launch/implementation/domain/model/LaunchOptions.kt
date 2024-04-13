package com.seancoyle.launch.implementation.domain.model

internal data class LaunchOptions(
    val options: Options
)

internal data class Options(
    val limit: Int,
    val populate: List<Populate>,
    val sort: Sort
)

internal data class Populate(
    val path: String,
    val select: Select
)

internal data class Select(
    val name: Int,
    val type: Int
)

internal data class Sort(
    val flightNumber: String
)