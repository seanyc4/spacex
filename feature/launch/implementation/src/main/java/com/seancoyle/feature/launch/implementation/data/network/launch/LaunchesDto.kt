package com.seancoyle.feature.launch.implementation.data.network.launch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LaunchesDto(
    @SerialName("docs")
    val launches: List<LaunchDto>,
)

@Serializable
internal data class LaunchDto(
    @SerialName("flight_number")
    val flightNumber: Int?,

    @SerialName("date_utc")
    val launchDate: String?,

    @SerialName("links")
    val links: LinksDto?,

    @SerialName("name")
    val missionName: String?,

    @SerialName("rocket")
    val rocket: RocketDto?,

    @SerialName("success")
    val isLaunchSuccess: Boolean?,
)

@Serializable
internal data class LinksDto(
    @SerialName("patch")
    val patch: PatchDto?,

    @SerialName("article")
    val articleLink: String?,

    @SerialName("webcast")
    val webcastLink: String?,

    @SerialName("wikipedia")
    val wikiLink: String?,
)

@Serializable
internal data class RocketDto(
    @SerialName("name")
    val name: String?,

    @SerialName("type")
    val type: String?,
)

@Serializable
internal data class PatchDto(
    @SerialName("small")
    val missionImage: String?
)