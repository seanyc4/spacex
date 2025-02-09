package com.seancoyle.feature.launch.implementation.data.network.mapper

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.feature.launch.implementation.data.network.dto.CompanyDto
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