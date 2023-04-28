package com.seancoyle.launch_datasource.cache

import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.launch_models.model.launch.LaunchModel
import com.seancoyle.launch_models.model.launch.LaunchType
import com.seancoyle.launch_models.model.launch.Links
import com.seancoyle.launch_models.model.launch.Rocket
import javax.inject.Inject

class LaunchEntityMapper
@Inject
constructor(){

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
                    webcastLink = links.videoLink,
                    wikiLink = links.wikipedia
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







