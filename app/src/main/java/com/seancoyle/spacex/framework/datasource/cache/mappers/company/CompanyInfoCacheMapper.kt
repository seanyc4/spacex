package com.seancoyle.spacex.framework.datasource.cache.mappers.company

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.util.EntityMapper
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoCacheEntity

class CompanyInfoCacheMapper : EntityMapper<CompanyInfoCacheEntity, CompanyInfoDomainEntity> {

    override fun mapFromEntity(entity: CompanyInfoCacheEntity): CompanyInfoDomainEntity {
        entity.apply {
            return CompanyInfoDomainEntity(
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

    override fun mapToEntity(domainModel: CompanyInfoDomainEntity): CompanyInfoCacheEntity {
        domainModel.apply {
            return CompanyInfoCacheEntity(
                id = id,
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







