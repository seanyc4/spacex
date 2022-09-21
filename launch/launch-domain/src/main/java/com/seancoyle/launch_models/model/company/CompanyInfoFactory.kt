package com.seancoyle.launch_models.model.company

import java.util.*
import javax.inject.Singleton

@Singleton
class CompanyInfoFactory {

    fun createCompanyInfo(
        id: String?,
        employees: String,
        founded: Int,
        founder: String,
        launchSites: Int,
        name: String,
        valuation: String
    ): CompanyInfoModel {
        return CompanyInfoModel(
            id = id ?: UUID.randomUUID().toString(),
            employees = employees,
            founded = founded,
            founder = founder,
            launchSites = launchSites,
            name = name,
            valuation = valuation,
        )
    }

}









