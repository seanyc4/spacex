package com.seancoyle.spacex.framework.datasource.cache.mappers.launch

import com.seancoyle.spacex.business.domain.model.launch.*
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchCacheEntity
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LinksCache
import com.seancoyle.spacex.framework.datasource.cache.model.launch.RocketCache

class LaunchCacheMapper {

    fun entityListToDomainList(entities: List<LaunchCacheEntity>): List<LaunchDomainEntity> {
        val list: ArrayList<LaunchDomainEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun domainListToEntityList(launchList: List<LaunchDomainEntity>): List<LaunchCacheEntity> {
        val entities: ArrayList<LaunchCacheEntity> = ArrayList()
        for (item in launchList) {
            entities.add(mapToEntity(item))
        }
        return entities
    }

    fun mapFromEntity(entity: LaunchCacheEntity): LaunchDomainEntity {
        entity.apply {
            return LaunchDomainEntity(
                id = id,
                launchDate = launchDate,
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

    fun mapToEntity(entity: LaunchDomainEntity): LaunchCacheEntity {
        entity.apply {
            return LaunchCacheEntity(
                id = id,
                launchDate = launchDate,
                launchSuccessIcon = launchSuccessIcon,
                launchYear = launchYear,
                links = LinksCache(
                    missionImage = links.missionImage,
                    articleLink = links.articleLink,
                    videoLink = links.videoLink,
                    wikipedia = links.wikipedia,
                ),
                missionName = missionName,
                rocket = RocketCache(
                    rocketNameAndType = rocket.rocketNameAndType
                ),
                daysToFromTitle = daysToFromTitle,
                launchDaysDifference = launchDaysDifference,
            )
        }
    }

}







