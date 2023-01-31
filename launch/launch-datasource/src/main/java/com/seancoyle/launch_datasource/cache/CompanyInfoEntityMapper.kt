package com.seancoyle.launch_datasource.cache

import com.seancoyle.database.entities.CompanyInfoEntity
import com.seancoyle.launch_models.model.company.CompanyInfoModel

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







