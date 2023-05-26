package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.launch.contract.domain.model.Links
import com.seancoyle.launch.contract.domain.model.Rocket
import com.seancoyle.launch.contract.domain.model.ViewModel
import com.seancoyle.launch.contract.domain.model.ViewType
import javax.inject.Inject

class LaunchEntityMapper @Inject constructor() {

    fun mapEntityListToDomainList(entities: List<LaunchEntity>): List<ViewModel> {
        return entities.map { entity -> mapFromEntity(entity) }
    }

    fun mapDomainListToEntityList(launches: List<ViewModel>): List<LaunchEntity> {
        return launches.map { item -> mapToEntity(item) }
    }

    fun mapFromEntity(entity: LaunchEntity): ViewModel {
        return with(entity) {
            ViewModel(
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

    fun mapToEntity(entity: ViewModel): LaunchEntity {
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