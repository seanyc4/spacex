package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.CompanyInfoEntity
import com.seancoyle.launch.contract.domain.model.CompanyInfo
import javax.inject.Inject

class CompanyInfoEntityMapper @Inject constructor() {

    fun mapFromEntity(entity: CompanyInfoEntity): CompanyInfo {
        return with(entity) {
            CompanyInfo(
                id = id,
                employees = employees,
                founded = founded,
                founder = founder,
                launchSites = launch_sites,
                name = name,
                valuation = valuation,
            )
        }
    }

    fun mapToEntity(domainModel: CompanyInfo): CompanyInfoEntity {
        return with(domainModel) {
            CompanyInfoEntity(
                id = "1",
                employees = employees,
                founded = founded,
                founder = founder,
                launch_sites = launchSites,
                name = name,
                valuation = valuation
            )
        }
    }
}