package com.seancoyle.feature.launch.implementation.data.network.company

import com.seancoyle.database.entities.CompanyEntity
import javax.inject.Inject

internal class CompanyDtoEntityMapper @Inject constructor() {

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
}