package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
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
                launchStatus = launchStatus,
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
                launchDateStatus = launchDateStatus,
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
                launchStatus = launchStatus,
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
                launchDateStatus = launchDateStatus,
                launchDays = launchDays,
                launchDaysResId = launchDaysResId,
                launchStatusIconResId = launchStatusIconResId
            )
        }
    }
}