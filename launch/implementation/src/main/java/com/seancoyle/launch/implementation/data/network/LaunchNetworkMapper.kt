package com.seancoyle.launch.implementation.data.network

import com.seancoyle.core.domain.DateFormatter
import com.seancoyle.core.domain.DateTransformer
import com.seancoyle.launch.api.LaunchConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_FAILED
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_SUCCESS
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_UNKNOWN
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchSuccessStatus
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.data.network.dto.LaunchDto
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchNetworkMapper @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) {

    fun mapEntityToList(entity: LaunchDto): List<Launch> {
        return entity.docs.map { item ->
            with(item) {
                val localDateTime = dateFormatter.formatDate(launchDate.orEmpty())
                val launchSuccess = isLaunchSuccess

                Launch(
                    id = flightNumber.toString()+localDateTime,
                    launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                    launchDateLocalDateTime = localDateTime,
                    isLaunchSuccess = mapIsLaunchSuccessToInt(item.isLaunchSuccess),
                    launchSuccessStatus = mapIsLaunchSuccessToStatus(launchSuccess),
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
                    launchDays = dateTransformer.getLaunchDaysDifference(localDateTime),
                    type = ViewType.TYPE_LIST
                )
            }
        }
    }

    private fun mapIsLaunchSuccessToInt(isLaunchSuccess: Boolean?) =
        when (isLaunchSuccess) {
            true -> { LAUNCH_SUCCESS }
            false -> { LAUNCH_FAILED }
            else -> { LAUNCH_UNKNOWN }
        }

    private fun mapIsLaunchSuccessToStatus(isLaunchSuccess: Boolean?) =
        when (isLaunchSuccess) {
            true -> { LaunchSuccessStatus.SUCCESS }
            false -> { LaunchSuccessStatus.FAILED }
            else -> { LaunchSuccessStatus.UNKNOWN }
        }

    private fun mapLaunchDateToStatus(localDateTime: LocalDateTime) =
        if (dateTransformer.isPastLaunch(localDateTime)) {
            LaunchDateStatus.PAST
        } else {
            LaunchDateStatus.FUTURE
        }
}