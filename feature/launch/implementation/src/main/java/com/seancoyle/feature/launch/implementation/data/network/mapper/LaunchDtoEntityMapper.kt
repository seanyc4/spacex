package com.seancoyle.feature.launch.implementation.data.network.mapper

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.database.entities.LaunchDateStatusEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.LinksEntity
import com.seancoyle.database.entities.RocketEntity
import com.seancoyle.feature.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchDto
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchDtoEntityMapper @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) {

    fun dtoToEntitynList(entities: List<LaunchDto>): List<LaunchEntity> {
        return entities.map { entity -> dtoToEntity(entity) }
    }

    fun dtoToEntity(entity: LaunchDto): LaunchEntity {
        return with(entity) {
                val localDateTime = dateFormatter.formatDate(launchDate.orEmpty())
                val launchSuccess = isLaunchSuccess

                LaunchEntity(
                    id = flightNumber.toString() + localDateTime,
                    launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                    launchDateLocalDateTime = localDateTime,
                    launchStatus = toLaunchStatusEntity(launchSuccess),
                    launchYear = dateTransformer.returnYearOfLaunch(localDateTime),
                    links = LinksEntity(
                        missionImage = links?.patch?.missionImage ?: DEFAULT_LAUNCH_IMAGE,
                        articleLink = links?.articleLink,
                        webcastLink = links?.webcastLink,
                        wikiLink = links?.wikiLink,
                    ),
                    missionName = missionName.orEmpty(),
                    rocket = RocketEntity(
                        rocketNameAndType = "${rocket?.name}/${rocket?.type}",
                    ),
                    launchDateStatus = mapToLaunchDateStatusEntity(localDateTime),
                    launchDays = dateTransformer.getLaunchDaysDifference(localDateTime),
                    launchDaysResId = 0,
                    launchStatusIconResId = 0
                )
            }
        }

    fun toLaunchStatusEntity(launchSuccess: Boolean?): LaunchStatusEntity {
        return when (launchSuccess) {
            true -> LaunchStatusEntity.SUCCESS
            false -> LaunchStatusEntity.FAILED
            null -> LaunchStatusEntity.UNKNOWN
        }
    }

    fun mapToLaunchDateStatusEntity(localDateTime: LocalDateTime): LaunchDateStatusEntity {
        return if (localDateTime.isBefore(LocalDateTime.now())) {
            LaunchDateStatusEntity.PAST
        } else {
            LaunchDateStatusEntity.FUTURE
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