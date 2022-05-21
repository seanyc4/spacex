package com.seancoyle.spacex.framework.datasource.network.model.company

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyInfoDto(

    @Expose
    @SerializedName("employees")
    val employees: Int?,

    @Expose
    @SerializedName("founded")
    val founded: Int?,

    @Expose
    @SerializedName("founder")
    val founder: String?,

    @Expose
    @SerializedName("launch_sites")
    val launch_sites: Int?,

    @Expose
    @SerializedName("name")
    val name: String?,

    @Expose
    @SerializedName("valuation")
    val valuation: Long?,

)
