package com.seancoyle.launch.implementation.domain.model

import androidx.annotation.Keep

@Keep
internal data class LaunchOptions(
    val options: Options
)

@Keep
internal data class Options(
    val limit: Int,
    val populate: List<Populate>,
    val sort: Sort
)

@Keep
internal data class Populate(
    val path: String,
    val select: Select
)

@Keep
internal data class Select(
    val name: Int,
    val type: Int
)

@Keep
internal data class Sort(
    val flightNumber: String
)

