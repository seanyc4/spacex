package com.seancoyle.launch_datasource.network.model.launch

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class LaunchDto(

    @Expose
    @SerializedName("docs")
    val docs: List<DocsDto>,
)

@Keep
data class DocsDto(

    @Expose
    @SerializedName("flight_number")
    val flightNumber: Int?,

    @Expose
    @SerializedName("date_utc")
    val launchDate: String?,

    @Expose
    @SerializedName("links")
    val links: LinksDto?,

    @Expose
    @SerializedName("name")
    val missionName: String?,

    @Expose
    @SerializedName("rocket")
    val rocket: RocketDto?,

    @Expose
    @SerializedName("success")
    val isLaunchSuccess: Boolean?,
)

@Keep
data class LinksDto(
    @Expose
    @SerializedName("patch")
    val patch: PatchDto?,

    @Expose
    @SerializedName("article")
    val articleLink: String?,

    @Expose
    @SerializedName("webcast")
    val videoLink: String?,

    @Expose
    @SerializedName("wikipedia")
    val wikipedia: String?,
)

@Keep
data class RocketDto(
    @Expose
    @SerializedName("name")
    val name: String?,

    @Expose
    @SerializedName("type")
    val type: String?,
)

@Keep
data class PatchDto(
    @Expose
    @SerializedName("small")
    val missionImage: String?
)


