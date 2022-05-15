package com.seancoyle.spacex.framework.datasource.network.model.launch

import com.google.gson.annotations.Expose

data class LaunchOptions(
    @Expose
    val options: Options
)

data class Options(
    @Expose
    val limit: Int,
    @Expose
    val populate: List<Populate>
)

data class Populate(
    @Expose
    val path: String,
    @Expose
    val select: Select
)

data class Select(
    @Expose
    val name: Int,
    @Expose
    val type: Int
)