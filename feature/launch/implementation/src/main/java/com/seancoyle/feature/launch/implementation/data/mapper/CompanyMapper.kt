package com.seancoyle.feature.launch.implementation.data.mapper

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.remote.company.CompanyDto
import javax.inject.Inject

internal class CompanyMapper @Inject constructor() {

    fun dtoToEntity(dto: CompanyDto): CompanyEntity {
        return with(dto) {
            CompanyEntity(
                id = "1",
                employees = employees ?: 0,
                founded = founded ?: 0,
                founder = founder.orEmpty(),
                launchSites = launchSites ?: 0,
                name = name.orEmpty(),
                valuation = valuation ?: 0L
            )
        }
    }

    fun dtoToDomain(dto: CompanyDto): Company {
        return with(dto) {
            Company(
                employees = employees ?: 0,
                founded = founded ?: 0,
                founder = founder.orEmpty(),
                launchSites = launchSites ?: 0,
                name = name.orEmpty(),
                valuation = valuation ?: 0L
            )
        }
    }

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
