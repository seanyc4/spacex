package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.api.domain.model.Company
import javax.inject.Inject

internal class CompanyEntityMapper @Inject constructor() {

    fun mapFromEntity(entity: CompanyEntity): Company {
        return with(entity) {
            Company(
                id = id,
                employees = employees,
                founded = founded,
                founder = founder,
                launchSites = launchSites,
                name = name,
                valuation = valuation,
            )
        }
    }

    fun mapToEntity(domainModel: Company): CompanyEntity {
        return with(domainModel) {
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