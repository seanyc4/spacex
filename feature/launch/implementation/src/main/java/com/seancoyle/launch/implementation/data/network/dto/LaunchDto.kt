package com.seancoyle.launch.implementation.data.network.dto

import com.google.gson.annotations.SerializedName

internal data class LaunchDto(
    @SerializedName("docs")
    val docs: List<DocsDto>,
)

internal data class DocsDto(
    @SerializedName("flight_number")
    val flightNumber: Int?,

    @SerializedName("date_utc")
    val launchDate: String?,

    @SerializedName("links")
    val links: LinksDto?,

    @SerializedName("name")
    val missionName: String?,

    @SerializedName("rocket")
    val rocket: RocketDto?,

    @SerializedName("success")
    val isLaunchSuccess: Boolean?,
)

internal data class LinksDto(
    @SerializedName("patch")
    val patch: PatchDto?,

    @SerializedName("article")
    val articleLink: String?,

    @SerializedName("webcast")
    val videoLink: String?,

    @SerializedName("wikipedia")
    val wikipedia: String?,
)

internal data class RocketDto(
    @SerializedName("name")
    val name: String?,

    @SerializedName("type")
    val type: String?,
)

internal data class PatchDto(
    @SerializedName("small")
    val missionImage: String?
)