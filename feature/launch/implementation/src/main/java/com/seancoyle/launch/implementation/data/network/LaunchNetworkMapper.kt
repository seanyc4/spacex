package com.seancoyle.launch.implementation.data.network

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.implementation.data.network.dto.LaunchDto
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchNetworkMapper @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) {

    fun mapEntityToList(entity: LaunchDto): List<LaunchTypes.Launch> {
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
                        webcastLink = links?.videoLink,
                        wikiLink = links?.wikipedia,
                    ),
                    missionName = missionName.orEmpty(),
                    rocket = Rocket(
                        rocketNameAndType = "${rocket?.name}/${rocket?.type}",
                    ),
                    launchDateStatus = mapLaunchDateToStatus(localDateTime),
                    launchDays = dateTransformer.getLaunchDaysDifference(localDateTime)
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