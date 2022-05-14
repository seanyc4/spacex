package com.seancoyle.spacex.framework.datasource.network.mappers.company

import com.seancoyle.spacex.business.domain.model.company.*
import com.seancoyle.spacex.framework.datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.spacex.framework.datasource.network.model.company.CompanyInfoNetworkEntity
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


class CompanyInfoNetworkMapper
@Inject
constructor(
    private val numberFormatter: NumberFormatter
){

    fun mapFromEntity(entity: CompanyInfoNetworkEntity): CompanyInfoDomainEntity {
        entity.apply {
            return CompanyInfoDomainEntity(
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







