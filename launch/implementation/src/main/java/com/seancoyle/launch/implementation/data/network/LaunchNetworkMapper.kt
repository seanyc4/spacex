package com.seancoyle.launch.implementation.data.network

import com.seancoyle.constants.LaunchNetworkConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_FAILED
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_SUCCESS
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_UNKNOWN
import com.seancoyle.core.util.DateFormatter
import com.seancoyle.core.util.DateTransformer
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchType
import com.seancoyle.launch.api.model.Links
import com.seancoyle.launch.api.model.Rocket
import com.seancoyle.launch.implementation.R
import com.seancoyle.launch.implementation.data.model.LaunchDto
import java.time.LocalDateTime
import javax.inject.Inject

class LaunchNetworkMapper @Inject constructor(
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) {

    fun mapEntityToList(entity: LaunchDto): List<LaunchModel> {
        return entity.docs.map { item ->
            val localDateTime = dateFormatter.formatDate(item.launchDate.orEmpty())
            val launchSuccess = item.isLaunchSuccess

            LaunchModel(
                id = item.flightNumber ?: 0,
                launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                launchDateLocalDateTime = localDateTime,
                isLaunchSuccess = mapIsLaunchSuccessToInt(item.isLaunchSuccess),
                launchSuccessIcon = setIsLaunchSuccessIcon(launchSuccess),
                launchYear = dateTransformer.returnYearOfLaunch(localDateTime),
                links = Links(
                    missionImage = item.links?.patch?.missionImage ?: DEFAULT_LAUNCH_IMAGE,
                    articleLink = item.links?.articleLink,
                    webcastLink = item.links?.videoLink,
                    wikiLink = item.links?.wikipedia,
                ),
                missionName = item.missionName.orEmpty(),
                rocket = Rocket(
                    rocketNameAndType = "${item.rocket?.name}/${item.rocket?.type}",
                ),
                daysToFromTitle = setCorrectLaunchText(localDateTime),
                launchDaysDifference = dateTransformer.getLaunchDaysDifference(localDateTime),
                type = LaunchType.TYPE_LAUNCH
            )
        }
    }

    private fun mapIsLaunchSuccessToInt(isLaunchSuccess: Boolean?) =
        when (isLaunchSuccess) {
            true -> {
                LAUNCH_SUCCESS
            }

            false -> {
                LAUNCH_FAILED
            }

            else -> {
                LAUNCH_UNKNOWN
            }
        }

    private fun setIsLaunchSuccessIcon(isLaunchSuccess: Boolean?) =
        when (isLaunchSuccess) {
            true -> {
                R.drawable.ic_launch_success
            }

            false -> {
                R.drawable.ic_launch_fail
            }

            else -> {
                R.drawable.ic_launch_unknown
            }
        }

    private fun setCorrectLaunchText(localDateTime: LocalDateTime) =
        if (isLaunchPastOrFuture(localDateTime = localDateTime)) {
            R.string.days_since_now
        } else {
            R.string.days_from_now
        }

    private fun isLaunchPastOrFuture(localDateTime: LocalDateTime) =
        dateTransformer.isPastLaunch(localDateTime)

}






