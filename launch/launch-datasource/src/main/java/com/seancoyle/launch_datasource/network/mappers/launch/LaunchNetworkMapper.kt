package com.seancoyle.launch_datasource.network.mappers.launch

import com.seancoyle.constants.LaunchNetworkMapperConstants.DEFAULT_LAUNCH_IMAGE
import com.seancoyle.constants.LaunchNetworkMapperConstants.LAUNCH_FAILED
import com.seancoyle.constants.LaunchNetworkMapperConstants.LAUNCH_SUCCESS
import com.seancoyle.constants.LaunchNetworkMapperConstants.LAUNCH_UNKNOWN
import com.seancoyle.launch_datasource.R
import com.seancoyle.launch_domain.model.launch.*
import com.seancoyle.launch_datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.launch_datasource.network.abstraction.datetransformer.DateTransformer
import com.seancoyle.launch_datasource.network.model.launch.LaunchDto
import java.time.LocalDateTime
import javax.inject.Inject

class LaunchNetworkMapper
@Inject
constructor(
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
                    articleLink = item.links?.articleLink.orEmpty(),
                    videoLink = item.links?.videoLink.orEmpty(),
                    wikipedia = item.links?.wikipedia.orEmpty(),
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







