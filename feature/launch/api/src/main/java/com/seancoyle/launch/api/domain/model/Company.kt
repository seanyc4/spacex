package com.seancoyle.launch.api.domain.model

data class Company(
    val id: String,
    val employees: String,
    val founded: Int,
    val founder: String,
    val launchSites: Int,
    val name: String,
    val valuation: String
)