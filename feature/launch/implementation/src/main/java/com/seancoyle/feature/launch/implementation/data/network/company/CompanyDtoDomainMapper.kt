package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.feature.launch.api.domain.model.Company
import javax.inject.Inject

internal class CompanyDtoDomainMapper @Inject constructor() {

    fun dtoToDomain(dto: CompanyDto): Company {
        return with(dto) {
            Company(
                employees = employees ?: 0,
                founded = founded ?: 0,
                founder = founder.orEmpty(),
                launchSites = launchSites ?: 0,
                name = name.orEmpty(),
                valuation = valuation ?: 0L,
            )
        }
    }
}