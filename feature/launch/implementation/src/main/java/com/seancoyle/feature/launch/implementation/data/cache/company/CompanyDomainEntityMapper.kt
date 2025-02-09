package com.seancoyle.feature.launch.implementation.data.cache.company

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.api.domain.model.Company
import javax.inject.Inject

internal class CompanyDomainEntityMapper @Inject constructor() {

    fun entityToDomain(entity: CompanyEntity): Company {
        return with(entity) {
            Company(
                employees = employees,
                founded = founded,
                founder = founder,
                launchSites = launchSites,
                name = name,
                valuation = valuation
            )
        }
    }

    fun domainToEntity(domain: Company): CompanyEntity {
        return with(domain) {
            CompanyEntity(
                id = "1",
                employees = employees,
                founded = founded,
                founder = founder,
                launchSites = launchSites,
                name = name,
                valuation = valuation
            )
        }
    }
}