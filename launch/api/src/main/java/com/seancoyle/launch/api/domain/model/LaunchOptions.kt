package com.seancoyle.launch.api.domain.model

import androidx.annotation.Keep

@Keep
data class LaunchOptions(
    val options: Options
)

@Keep
data class Options(
    val limit: Int,
    val populate: List<Populate>,
    val sort: Sort
)

@Keep
data class Populate(
    val path: String,
    val select: Select
)

@Keep
data class Select(
    val name: Int,
    val type: Int
)

@Keep
data class Sort(
    val flight_number: String
)

