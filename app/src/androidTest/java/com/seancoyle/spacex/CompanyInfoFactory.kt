package com.seancoyle.spacex

import com.seancoyle.launch.contract.domain.model.CompanyInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyInfoFactory
@Inject constructor(){

    fun createCompanyInfo(
        id: String?,
        employees: String,
        founded: Int,
        founder: String,
        launchSites: Int,
        name: String,
        valuation: String
    ): CompanyInfo {
        return CompanyInfo(
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