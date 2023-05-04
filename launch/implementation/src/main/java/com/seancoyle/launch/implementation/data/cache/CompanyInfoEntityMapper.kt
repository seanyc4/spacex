package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.CompanyInfoEntity
import com.seancoyle.launch.api.model.CompanyInfoModel
import javax.inject.Inject

class CompanyInfoEntityMapper
@Inject
constructor(){

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

     fun mapToEntity(domainModel:CompanyInfoModel): CompanyInfoEntity {
        domainModel.apply {
            return CompanyInfoEntity(
                id = "1",
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







