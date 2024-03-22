package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core_database.api.CompanyEntity
import com.seancoyle.launch.api.domain.model.Company

internal interface CompanyEntityMapper {
    fun mapFromEntity(entity: CompanyEntity): Company
    fun mapToEntity(domainModel: Company): CompanyEntity
}