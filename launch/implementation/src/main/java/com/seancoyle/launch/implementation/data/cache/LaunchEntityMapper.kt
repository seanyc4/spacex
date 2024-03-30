package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType

internal interface LaunchEntityMapper {
    fun mapEntityListToDomainList(entities: List<LaunchEntity>): List<ViewType>
    fun mapDomainListToEntityList(launches: List<Launch>): List<LaunchEntity>
    fun mapFromEntity(entity: LaunchEntity): Launch
    fun mapToEntity(entity: Launch): LaunchEntity
}