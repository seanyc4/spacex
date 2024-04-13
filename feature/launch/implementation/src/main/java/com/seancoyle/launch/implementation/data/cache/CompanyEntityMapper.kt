package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.launch.api.domain.model.Company
import javax.inject.Inject

internal class CompanyEntityMapper @Inject constructor() {

    fun mapFromEntity(entity: CompanyEntity): Company {
        return with(entity) {
            Company(
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

    fun mapToEntity(domainModel: Company): CompanyEntity {
        return with(domainModel) {
            CompanyEntity(
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