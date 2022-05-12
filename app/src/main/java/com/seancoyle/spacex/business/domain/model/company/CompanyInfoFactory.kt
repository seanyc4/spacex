package com.seancoyle.spacex.business.domain.model.company

import java.util.*
import javax.inject.Singleton

@Singleton
class CompanyInfoFactory {

    fun createCompanyInfo(
        id: String?,
        employees: Int,
        founded: Int,
        founder: String,
        launchSites: Int,
        name: String,
        valuation: Long
    ): CompanyInfoDomainEntity {
        return CompanyInfoDomainEntity(
            id = id?: UUID.randomUUID().toString(),
            employees = employees,
            founded = founded,
            founder = founder,
            launchSites = launchSites,
            name = name,
            valuation = valuation,
        )
    }

}









