package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.launch.api.domain.model.LaunchTypes

internal interface LaunchEntityMapper {
    fun mapEntityListToDomainList(entities: List<LaunchEntity>): List<LaunchTypes>
    fun mapDomainListToEntityList(launches: List<LaunchTypes.Launch>): List<LaunchEntity>
    fun mapFromEntity(entity: LaunchEntity): LaunchTypes.Launch
    fun mapToEntity(entity: LaunchTypes.Launch): LaunchEntity
}