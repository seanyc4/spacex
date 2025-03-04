package com.seancoyle.feature.launch.implementation.data.mapper

import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import java.time.LocalDateTime

import javax.inject.Inject

internal class LaunchMapper @Inject constructor() {

    fun entityToDomainList(entities: List<LaunchEntity>): List<LaunchTypes.Launch> {
        return entities.map { entity -> entityToDomain(entity) }
    }

    fun domainToEntityList(launches: List<LaunchTypes.Launch>): List<LaunchEntity> {
        return launches.map { item -> domainToEntity(item) }
    }

    fun entityToDomain(entity: LaunchEntity): LaunchTypes.Launch {
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
                isLaunchSuccess = isLaunchSuccess
            )
        }
    }

    fun domainToEntity(entity: LaunchTypes.Launch): LaunchEntity {
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
                isLaunchSuccess = isLaunchSuccess
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

    fun dtoToDomainList(entity: LaunchesDto): List<LaunchTypes.Launch> {
        return entity.launches.map { item ->
            with(item) {
                LaunchTypes.Launch(
                    id = flightNumber.toString(),
                    launchDate = launchDate.orEmpty(),
                    links = Links(
                        missionImage = links?.patch?.missionImage ?: DEFAULT_LAUNCH_IMAGE,
                        articleLink = links?.articleLink,
                        webcastLink = links?.webcastLink,
                        wikiLink = links?.wikiLink,
                    ),
                    missionName = missionName.orEmpty(),
                    rocket = Rocket(
                        rocketNameAndType = "${rocket?.name}/${rocket?.type}",
                    ),
                    isLaunchSuccess = isLaunchSuccess ?: false,
                    launchDateLocalDateTime = LocalDateTime.now(),
                    launchYear = "",
                    launchStatus = LaunchStatus.UNKNOWN,
                    launchDateStatus = LaunchDateStatus.PAST,
                    launchDays = "",
                )
            }
        }
    }

}