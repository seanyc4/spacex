package com.seancoyle.spacex.framework.datasource.network.model.launch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LaunchDto(

    @Expose
    @SerializedName("docs")
    val docs: List<DocsDto>,
    )

data class DocsDto(

    @Expose
    @SerializedName("flight_number")
    val flightNumber: Int?,

    @Expose
    @SerializedName("date_utc")
    val launchDate: String?,

    @Expose
    val links: LinksDto,

    @Expose
    @SerializedName("name")
    val missionName: String?,

    @Expose
    val rocket: RocketDto,

    @Expose
    @SerializedName("success")
    val isLaunchSuccess: Boolean?,

    )

data class LinksDto(
    @Expose
    val patch: PatchDto,

    @Expose
    @SerializedName("article")
    val articleLink: String?,

    @Expose
    @SerializedName("webcast")
    val videoLink: String?,

    @Expose
    val wikipedia: String?,

    )

data class RocketDto(
    @Expose
    val name: String?,

    @Expose
    val type: String?,
)

data class PatchDto(
    @Expose
    @SerializedName("small")
    val missionImage: String?
)


