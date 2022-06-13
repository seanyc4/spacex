package com.seancoyle.spacex.framework.datasource.cache.mappers.company

import com.seancoyle.launch_domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoEntity

class CompanyInfoEntityMapper {

     fun mapFromEntity(entity: CompanyInfoEntity): CompanyInfoModel {
        entity.apply {
            return CompanyInfoModel(
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

     fun mapToEntity(domainModel: CompanyInfoModel): CompanyInfoEntity {
        domainModel.apply {
            return CompanyInfoEntity(
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







