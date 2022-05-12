package com.seancoyle.spacex.framework.datasource.network.mappers.company

import com.seancoyle.spacex.business.domain.model.company.*
import com.seancoyle.spacex.framework.datasource.network.model.company.CompanyInfoNetworkEntity


class CompanyInfoNetworkMapper {

    fun mapFromEntity(entity: CompanyInfoNetworkEntity): CompanyInfoDomainEntity {
        entity.apply {
            return CompanyInfoDomainEntity(
                id = "",
                employees = employees ?: 0,
                founded = founded ?: 0,
                founder = founder.orEmpty(),
                launchSites = launch_sites ?: 0,
                name = name.orEmpty(),
                valuation = valuation ?: 0
            )
        }
    }

}







