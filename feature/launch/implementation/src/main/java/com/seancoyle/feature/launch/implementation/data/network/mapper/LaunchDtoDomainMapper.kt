package com.seancoyle.feature.launch.implementation.data.network.mapper

import com.seancoyle.feature.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchDtoDomainMapper @Inject constructor() {

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