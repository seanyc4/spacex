package com.seancoyle.spacex.framework.datasource.network.model.launch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LaunchNetworkEntity(

    @Expose
    @SerializedName("docs")
    val docs: List<Docs>,
    )

data class Docs(

    @Expose
    @SerializedName("flight_number")
    val flightNumber: Int?,

    @Expose
    @SerializedName("date_utc")
    val launchDate: String?,

    @Expose
    val links: LinksNetwork,

    @Expose
    @SerializedName("name")
    val missionName: String?,

    @Expose
    val rocket: RocketNetwork,

    @Expose
    @SerializedName("success")
    val isLaunchSuccess: Boolean?,

    )

data class LinksNetwork(
    @Expose
    val patch: PatchNetwork,

    @Expose
    @SerializedName("article")
    val articleLink: String?,

    @Expose
    @SerializedName("webcast")
    val videoLink: String?,

    @Expose
    val wikipedia: String?,

    )

data class RocketNetwork(
    @Expose
    val name: String?,

    @Expose
    val type: String?,
)

data class PatchNetwork(
    @Expose
    @SerializedName("small")
    val missionImage: String?
)


