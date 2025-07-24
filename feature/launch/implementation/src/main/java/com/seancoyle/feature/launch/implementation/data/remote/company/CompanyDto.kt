package com.seancoyle.feature.launch.implementation.data.remote.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CompanyDto(
    @SerialName("employees")
    val employees: Int?,

    @SerialName("founded")
    val founded: Int?,

    @SerialName("founder")
    val founder: String?,

    @SerialName("launch_sites")
    val launchSites: Int?,

    @SerialName("name")
    val name: String?,

    @SerialName("valuation")
    val valuation: Long?,
)
