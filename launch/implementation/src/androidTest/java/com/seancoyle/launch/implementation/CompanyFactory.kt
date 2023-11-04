package com.seancoyle.launch.implementation

import com.seancoyle.launch.api.domain.model.Company
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyFactory
@Inject constructor(){

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