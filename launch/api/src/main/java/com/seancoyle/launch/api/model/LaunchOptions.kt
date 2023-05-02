package com.seancoyle.launch.api.model

data class LaunchOptions(
    val options: Options
)

data class Options(
    val limit: Int,
    val populate: List<Populate>,
    val sort: Sort
)

data class Populate(
    val path: String,
    val select: Select
)

data class Select(
    val name: Int,
    val type: Int
)

data class Sort(
    val flight_number: String
)

