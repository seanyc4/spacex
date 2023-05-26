package com.seancoyle.launch.implementation.data.network

import com.seancoyle.core.presentation.util.NumberFormatter
import com.seancoyle.launch.contract.domain.model.CompanyInfo
import com.seancoyle.launch.implementation.data.model.CompanyInfoDto
import javax.inject.Inject

class CompanyInfoNetworkMapper @Inject constructor(
    private val numberFormatter: NumberFormatter
) {

    fun mapFromEntity(dto: CompanyInfoDto): CompanyInfo {
        return with(dto) {
            CompanyInfo(
                id = "",
                employees = numberFormatter.formatNumber(employees?.toLong()),
                founded = founded ?: 0,
                founder = founder.orEmpty(),
                launchSites = launch_sites ?: 0,
                name = name.orEmpty(),
                valuation = numberFormatter.formatNumber(valuation)
            )
        }
    }
}