package com.seancoyle.feature.launch.implementation.data.cache.mapper

import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket

import javax.inject.Inject

internal class LaunchEntityMapper @Inject constructor() {

    fun entityToDomainList(entities: List<LaunchEntity>): List<LaunchTypes> {
        return entities.map { entity -> mapFromEntity(entity) }
    }

    fun domainToEntityList(launches: List<LaunchTypes.Launch>): List<LaunchEntity> {
        return launches.map { item -> mapToEntity(item) }
    }

    fun mapFromEntity(entity: LaunchEntity): LaunchTypes.Launch {
        return with(entity) {
            LaunchTypes.Launch(
                id = id,
                launchDate = launchDate,
                launchDateLocalDateTime = launchDateLocalDateTime,
                launchStatus = mapToLaunchStatus(launchStatus),
                launchYear = launchYear,
                links = Links(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    webcastLink = links.webcastLink,
                    wikiLink = links.wikiLink
                ),
                missionName = missionName,
                rocket = Rocket(
                    rocketNameAndType = rocket.rocketNameAndType
                ),
                launchDateStatus = mapToLaunchDateStatus(launchDateStatus),
                launchDays = launchDays,
                launchDaysResId = launchDaysResId,
                launchStatusIconResId = launchStatusIconResId
            )
        }
    }

    fun mapToEntity(entity: LaunchTypes.Launch): LaunchEntity {
        return with(entity) {
            LaunchEntity(
                id = id,
                launchDate = launchDate,
                launchDateLocalDateTime = launchDateLocalDateTime,
                launchStatus = mapToLaunchStatusEntity(launchStatus),
                launchYear = launchYear,
                links = LinksEntity(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    webcastLink = links.webcastLink,
                    wikiLink = links.wikiLink,
                ),
                missionName = missionName,
                rocket = RocketEntity(
                    rocketNameAndType = rocket.rocketNameAndType
                ),
                launchDateStatus = mapToLaunchDateStatusEntity(launchDateStatus),
                launchDays = launchDays,
                launchDaysResId = launchDaysResId,
                launchStatusIconResId = launchStatusIconResId
            )
        }
    }

    fun mapToLaunchStatusEntity(launchStatus: LaunchStatus): LaunchStatusEntity {
        return when (launchStatus) {
            LaunchStatus.SUCCESS -> LaunchStatusEntity.SUCCESS
            LaunchStatus.FAILED -> LaunchStatusEntity.FAILED
            LaunchStatus.UNKNOWN -> LaunchStatusEntity.UNKNOWN
            LaunchStatus.ALL -> LaunchStatusEntity.ALL
        }
    }

    fun mapToLaunchDateStatusEntity(launchDateStatus: LaunchDateStatus): LaunchDateStatusEntity {
        return when (launchDateStatus) {
            LaunchDateStatus.PAST -> LaunchDateStatusEntity.PAST
            LaunchDateStatus.FUTURE -> LaunchDateStatusEntity.FUTURE
        }
    }

    fun mapToLaunchStatus(launchStatusEntity: LaunchStatusEntity): LaunchStatus {
        return when (launchStatusEntity) {
            LaunchStatusEntity.SUCCESS -> LaunchStatus.SUCCESS
            LaunchStatusEntity.FAILED -> LaunchStatus.FAILED
            LaunchStatusEntity.UNKNOWN -> LaunchStatus.UNKNOWN
            LaunchStatusEntity.ALL -> LaunchStatus.ALL
        }
    }

    fun mapToLaunchDateStatus(launchDateStatusEntity: LaunchDateStatusEntity): LaunchDateStatus {
        return when (launchDateStatusEntity) {
            LaunchDateStatusEntity.PAST -> LaunchDateStatus.PAST
            LaunchDateStatusEntity.FUTURE -> LaunchDateStatus.FUTURE
        }
    }
}