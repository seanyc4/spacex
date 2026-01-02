package com.seancoyle.feature.launch.presentation

import com.seancoyle.core.common.dataformatter.DateFormatConstants
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.Status
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import java.time.LocalDateTime
import javax.inject.Inject

class LaunchUiMapper @Inject constructor(
    private val dateFormatter: DateTransformer
) {

    fun mapToLaunchUi(launch: Launch): LaunchUI {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            val windowStart = windowStart?.let { dateFormatter.formatDate(it) }
            val windowEnd = windowEnd?.let { dateFormatter.formatDate(it) }
            return LaunchUI(
                id = id,
                missionName = missionName.substringBefore("|").trim(),
                launchDate = formatDate(locateDateTime),
                status = status.toDomain(),
                windowEnd = formatDate(windowEnd, DateFormatConstants.DD_MMMM_YYYY_AT_HH_MM),
                windowStart = formatDate(windowStart, DateFormatConstants.DD_MMMM_YYYY_AT_HH_MM),
                image = image,
                failReason = failReason,
                launchServiceProvider = launchServiceProvider,
                rocket = rocket,
                mission = mission,
                pad = pad,
                updates = updates ?: emptyList(),
                infoUrls = infoUrls ?: emptyList(),
                vidUrls = vidUrls ?: emptyList(),
                missionPatches = missionPatches ?: emptyList()
            )
        }
    }

    fun mapToLaunchesUi(launch: Launch): LaunchesUi {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            return LaunchesUi(
                id = id,
                launchDate = formatDate(locateDateTime),
                missionName = missionName,
                status = status.toDomain(),
                image = image.thumbnailUrl
            )
        }
    }

    private fun formatDate(date: LocalDateTime?, format: String = DateFormatConstants.DD_MMMM_YYYY): String {
        return dateFormatter.formatDateTimeToString(date, format)
    }

    fun Status?.toDomain(): LaunchStatus =
        when {
            this?.abbrev?.contains("Success", ignoreCase = true) == true -> LaunchStatus.SUCCESS
            this?.abbrev?.contains("Go", ignoreCase = true) == true -> LaunchStatus.GO
            this?.abbrev?.contains("Fail", ignoreCase = true) == true -> LaunchStatus.FAILED
            this?.abbrev?.contains("To Be Confirmed", ignoreCase = true) == true -> LaunchStatus.TBC
            this?.abbrev?.contains("To Be Determined", ignoreCase = true) == true -> LaunchStatus.TBD
            else -> LaunchStatus.TBD
        }
}