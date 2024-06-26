package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.numberformatter.NumberFormatter
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.network.dto.CompanyDto
import javax.inject.Inject

internal class CompanyNetworkMapper @Inject constructor(
    private val numberFormatter: NumberFormatter
) {

    fun mapFromEntity(dto: CompanyDto): Company {
        return with(dto) {
            Company(
                id = "",
                employees = numberFormatter.formatNumber(employees?.toLong()),
                founded = founded ?: 0,
                founder = founder.orEmpty(),
                launchSites = launchSites ?: 0,
                name = name.orEmpty(),
                valuation = numberFormatter.formatNumber(valuation),
                summary = ""
            )
        }
    }
}