package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyEntityMapper {
    fun mapFromEntity(entity: CompanyEntity): Company
    fun mapToEntity(domainModel: Company): CompanyEntity
}