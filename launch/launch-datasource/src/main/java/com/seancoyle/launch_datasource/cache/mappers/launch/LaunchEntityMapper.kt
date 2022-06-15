package com.seancoyle.launch_datasource.cache.mappers.launch

import com.seancoyle.data.entities.LaunchEntity
import com.seancoyle.data.entities.LinksEntity
import com.seancoyle.data.entities.RocketEntity
import com.seancoyle.launch_domain.model.launch.*

class LaunchEntityMapper {

    fun entityListToDomainList(entities: List<LaunchEntity>): List<LaunchModel> {
        val list: ArrayList<LaunchModel> = ArrayList()
        for (entity in entities) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun domainListToEntityList(launchList: List<LaunchModel>): List<LaunchEntity> {
        val entities: ArrayList<LaunchEntity> = ArrayList()
        for (item in launchList) {
            entities.add(mapToEntity(item))
        }
        return entities
    }

    fun mapFromEntity(entity: LaunchEntity): LaunchModel {
        entity.apply {
            return LaunchModel(
                id = id,
                launchDate = launchDate,
                launchDateLocalDateTime = launchDateLocalDateTime,
                isLaunchSuccess = isLaunchSuccess,
                launchSuccessIcon = launchSuccessIcon,
                launchYear = launchYear,
                links = Links(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    videoLink = links.videoLink,
                    wikipedia = links.wikipedia
                ),
                missionName = missionName,
                rocket = Rocket(
                    rocketNameAndType = rocket.rocketNameAndType
                ),
                daysToFromTitle = daysToFromTitle,
                launchDaysDifference = launchDaysDifference,
                type = LaunchType.TYPE_LAUNCH
            )
        }
    }

    fun mapToEntity(entity: LaunchModel): LaunchEntity {
        entity.apply {
            return LaunchEntity(
                id = id,
                launchDate = launchDate,
                launchDateLocalDateTime = launchDateLocalDateTime,
                isLaunchSuccess = isLaunchSuccess,
                launchSuccessIcon = launchSuccessIcon,
                launchYear = launchYear,
                links = LinksEntity(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    videoLink = links.videoLink,
                    wikipedia = links.wikipedia,
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







