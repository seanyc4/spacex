package com.seancoyle.spacex.framework.datasource.network.model.launch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LaunchNetworkEntity(

    @Expose
    @SerializedName("flight_number")
    val flightNumber: Int?,

    @Expose
    @SerializedName("launch_date_utc")
    val launchDate: String?,

    @Expose
    @SerializedName("launch_success")
    val isLaunchSuccess: Boolean?,

    @Expose
    @SerializedName("launch_year")
    val launchYear: String?,

    @Expose
    val links: LinksNetwork,

    @Expose
    @SerializedName("mission_name")
    val missionName: String?,

    @Expose
    val rocket: RocketNetwork,
)

data class LinksNetwork(
    @Expose
    @SerializedName("mission_patch_small")
    val missionImage: String?,

    @Expose
    @SerializedName("article_link")
    val articleLink: String?,

    @Expose
    @SerializedName("video_link")
    val videoLink: String?,

    @Expose
    @SerializedName("wikipedia")
    val wikipedia: String?,

)

data class RocketNetwork(

    @Expose
    @SerializedName("rocket_name")
    val rocketName: String?,

    @Expose
    @SerializedName("rocket_type")
    val rocketType: String?,
)










