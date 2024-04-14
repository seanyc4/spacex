package com.seancoyle.feature.launch.implementation

import com.seancoyle.feature.launch.api.domain.model.Company
import javax.inject.Inject

internal class CompanyFactory @Inject constructor() {
    fun createCompany(
        id: String?,
        employees: String,
        founded: Int,
        founder: String,
        launchSites: Int,
        name: String,
        valuation: String
    ): Company {
        return Company(
            id = id ?: "1",
            employees = employees,
            founded = founded,
            founder = founder,
            launchSites = launchSites,
            name = name,
            valuation = valuation,
        )
    }
}