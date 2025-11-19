package com.seancoyle.feature.launch.api.domain.model

data class Company(
    val employees: Int,
    val founded: Int,
    val founder: String,
    val launchSites: Int,
    val name: String,
    val valuation: Long,
)
