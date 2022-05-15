package com.seancoyle.spacex.framework.datasource.network.mappers.launch

import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.domain.model.launch.*
import com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchNetworkEntity
import java.time.LocalDateTime
import javax.inject.Inject

const val DEFAULT_LAUNCH_IMAGE = "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png"

class LaunchNetworkMapper
@Inject
constructor(
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) {

    fun mapEntityToList(entity: LaunchNetworkEntity): List<LaunchDomainEntity> {
        return entity.docs.map { item ->
            val localDateTime = dateFormatter.formatDate(item.launchDate.orEmpty())
            val launchSuccess = item.isLaunchSuccess ?: false

            LaunchDomainEntity(
                id = item.flightNumber ?: 0,
                launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                launchDateLocalDateTime = localDateTime,
                isLaunchSuccess = item.isLaunchSuccess ?: false,
                launchSuccessIcon = isLaunchSuccess(launchSuccess),
                launchYear = dateTransformer.returnYearOfLaunch(localDateTime),
                links = Links(
                    missionImage = item.links.patch.missionImage ?: DEFAULT_LAUNCH_IMAGE,
                    articleLink = item.links.articleLink.orEmpty(),
                    videoLink = item.links.videoLink.orEmpty(),
                    wikipedia = item.links.wikipedia.orEmpty(),
                ),
                missionName =item. missionName.orEmpty(),
                rocket = Rocket(
                    rocketNameAndType = "${item.rocket.name}/${item.rocket.type}",
                ),
                daysToFromTitle = setCorrectLaunchText(localDateTime),
                launchDaysDifference = dateTransformer.getLaunchDaysDifference(localDateTime),
                type = LaunchType.TYPE_LAUNCH
            )
        }
    }

    private fun isLaunchSuccess(isLaunchSuccess: Boolean) =
        if (isLaunchSuccess) {
            R.drawable.ic_launch_success
        } else {
            R.drawable.ic_launch_fail
        }

    private fun setCorrectLaunchText(localDateTime: LocalDateTime) =
        if (isLaunchPastOrFuture(localDateTime = localDateTime)) {
            R.string.days_since_now
        } else {
            R.string.days_from_now
        }

    private fun isLaunchPastOrFuture(localDateTime: LocalDateTime)
            = dateTransformer.isPastLaunch(localDateTime)

}







