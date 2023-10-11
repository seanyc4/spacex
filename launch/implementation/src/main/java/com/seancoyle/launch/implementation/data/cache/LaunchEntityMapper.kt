package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType
import javax.inject.Inject

class LaunchEntityMapper @Inject constructor() {

    fun mapEntityListToDomainList(entities: List<LaunchEntity>): List<ViewType> {
        return entities.map { entity -> mapFromEntity(entity) }
    }

    fun mapDomainListToEntityList(launches: List<Launch>): List<LaunchEntity> {
        return launches.map { item -> mapToEntity(item) }
    }

    fun mapFromEntity(entity: LaunchEntity): Launch {
        return with(entity) {
            Launch(
                id = id,
                launchDate = launchDate,
                launchDateLocalDateTime = launchDateLocalDateTime,
                isLaunchSuccess = isLaunchSuccess,
                launchSuccessIcon = launchSuccessIcon,
                launchYear = launchYear,
                links = Links(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    webcastLink = links.videoLink,
                    wikiLink = links.wikipedia
                ),
                missionName = missionName,
                rocket = Rocket(
                    rocketNameAndType = rocket.rocketNameAndType
                ),
                daysToFromTitle = daysToFromTitle,
                launchDaysDifference = launchDaysDifference,
                type = ViewType.TYPE_LIST
            )
        }
    }

    fun mapToEntity(entity: Launch): LaunchEntity {
        return with(entity) {
            LaunchEntity(
                id = id,
                launchDate = launchDate,
                launchDateLocalDateTime = launchDateLocalDateTime,
                isLaunchSuccess = isLaunchSuccess,
                launchSuccessIcon = launchSuccessIcon,
                launchYear = launchYear,
                links = LinksEntity(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    videoLink = links.webcastLink,
                    wikipedia = links.wikiLink,
                ),
                missionName = missionName,
                rocket = RocketEntity(
                    rocketNameAndType = rocket.rocketNameAndType
                ),
                daysToFromTitle = daysToFromTitle,
                launchDaysDifference = launchDaysDifference,
            )
        }
    }
}