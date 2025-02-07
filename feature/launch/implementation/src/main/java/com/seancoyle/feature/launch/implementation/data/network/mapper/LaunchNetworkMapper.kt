package com.seancoyle.feature.launch.implementation.data.network.mapper

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.api.domain.model.Rocket
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchNetworkMapper @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) {

    fun mapEntityToList(entity: LaunchesDto): List<LaunchTypes.Launch> {
        return entity.docs.map { item ->
            with(item) {
                val localDateTime = dateFormatter.formatDate(launchDate.orEmpty())
                val launchSuccess = isLaunchSuccess

                LaunchTypes.Launch(
                    id = flightNumber.toString() + localDateTime,
                    launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                    launchDateLocalDateTime = localDateTime,
                    launchStatus = mapIsLaunchSuccessToStatus(launchSuccess),
                    launchYear = dateTransformer.returnYearOfLaunch(localDateTime),
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
                    launchDateStatus = mapLaunchDateToStatus(localDateTime),
                    launchDays = dateTransformer.getLaunchDaysDifference(localDateTime),
                    launchDaysResId = 0,
                    launchStatusIconResId = 0
                )
            }
        }
    }

    private fun mapIsLaunchSuccessToStatus(isLaunchSuccess: Boolean?) =
        when (isLaunchSuccess) {
            true -> { LaunchStatus.SUCCESS }
            false -> { LaunchStatus.FAILED }
            else -> { LaunchStatus.UNKNOWN }
        }

    private fun mapLaunchDateToStatus(localDateTime: LocalDateTime) =
        if (dateTransformer.isPastLaunch(localDateTime)) {
            LaunchDateStatus.PAST
        } else {
            LaunchDateStatus.FUTURE
        }
}