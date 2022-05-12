package com.seancoyle.spacex.framework.datasource.network.model.company

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CompanyInfoNetworkEntity(
    @Expose
    @SerializedName("ceo")
    val ceo: String?,
    @Expose
    @SerializedName("coo")
    val coo: String?,
    @Expose
    @SerializedName("cto")
    val cto: String?,
    @Expose
    @SerializedName("cto_propulsion")
    val cto_propulsion: String?,
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
    @SerializedName("headquarters")
    val headquarters: HeadquartersNetwork,
    @Expose
    @SerializedName("launch_sites")
    val launch_sites: Int?,
    @Expose
    @SerializedName("links")
    val links: LinksNetwork,
    @Expose
    @SerializedName("name")
    val name: String?,
    @Expose
    @SerializedName("summary")
    val summary: String?,
    @Expose
    @SerializedName("test_sites")
    val test_sites: Int?,
    @Expose
    @SerializedName("valuation")
    val valuation: Long?,
    @Expose
    @SerializedName("vehicles")
    val vehicles: Int?
)

data class HeadquartersNetwork(
    @Expose
    @SerializedName("address")
    val address: String?,
    @Expose
    @SerializedName("city")
    val city: String?,
    @Expose
    @SerializedName("state")
    val state: String?
)

data class LinksNetwork(
    @Expose
    @SerializedName("elon_twitter")
    val elon_twitter: String?,
    @Expose
    @SerializedName("flickr")
    val flickr: String?,
    @Expose
    @SerializedName("twitter")
    val twitter: String?,
    @Expose
    @SerializedName("website")
    val website: String?
)