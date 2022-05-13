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

    fun entityListToDomainList(entities: List<LaunchNetworkEntity>): List<LaunchDomainEntity> {
        val list: ArrayList<LaunchDomainEntity> = ArrayList()
        for (entity in entities) {
            list.add(mapFromEntity(entity))
        }
        return list
    }

    private fun mapFromEntity(entity: LaunchNetworkEntity): LaunchDomainEntity {
        entity.apply {
            val localDateTime = dateFormatter.formatDate(launchDate.orEmpty())
            val launchSuccess = isLaunchSuccess ?: false
            return LaunchDomainEntity(
                id = flightNumber ?: 0,
                launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                launchDateLocalDateTime = localDateTime,
                isLaunchSuccess = isLaunchSuccess?: false,
                launchSuccessIcon = isLaunchSuccess(launchSuccess),
                launchYear = launchYear.orEmpty(),
                links = Links(
                    missionImage = links.missionImage ?: DEFAULT_LAUNCH_IMAGE,
                    articleLink = links.articleLink.orEmpty(),
                    videoLink = links.videoLink.orEmpty(),
                    wikipedia = links.wikipedia.orEmpty(),
                ),
                missionName = missionName.orEmpty(),
                rocket = Rocket(
                    rocketNameAndType = "${rocket.rocketName}/${rocket.rocketType}",
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







