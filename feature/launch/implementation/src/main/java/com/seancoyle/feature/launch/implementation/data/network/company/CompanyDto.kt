package com.seancoyle.feature.launch.implementation.data.network.company

import com.google.gson.annotations.SerializedName

internal data class CompanyDto(
    @SerializedName("employees")
    val employees: Int?,

    @SerializedName("founded")
    val founded: Int?,

    @SerializedName("founder")
    val founder: String?,

    @SerializedName("launch_sites")
    val launchSites: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("valuation")
    val valuation: Long?,
)