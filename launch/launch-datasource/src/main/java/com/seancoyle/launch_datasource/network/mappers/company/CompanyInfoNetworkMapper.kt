package com.seancoyle.launch_datasource.network.mappers.company

import com.seancoyle.launch_datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.launch_datasource.network.model.company.CompanyInfoDto
import com.seancoyle.launch_models.model.company.CompanyInfoModel
import javax.inject.Inject


class CompanyInfoNetworkMapper
@Inject
constructor(
    private val numberFormatter: NumberFormatter
){

    fun mapFromEntity(dto: CompanyInfoDto): CompanyInfoModel {
        dto.apply {
            return CompanyInfoModel(
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







