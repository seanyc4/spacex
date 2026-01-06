package com.seancoyle.feature.launch.presentation

import com.seancoyle.core.common.dataformatter.DateFormatConstants
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
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
            val launchDateTime = dateFormatter.formatDate(net)
            val windowStartDateTime = windowStart?.let { dateFormatter.formatDate(it) }
            val windowEndDateTime = windowEnd?.let { dateFormatter.formatDate(it) }
            val duration = calculateDuration(windowStartDateTime, windowEndDateTime)

            return LaunchUI(
                id = id,
                missionName = missionName.substringBefore("|").trim(),
                launchDate = formatDate(launchDateTime),
                launchTime = formatDate(launchDateTime, DateFormatConstants.HH_MM),
                launchDateTime = launchDateTime,
                status = status.toDomain(),
                windowEnd = formatDate(windowEndDateTime, DateFormatConstants.DD_MMMM_YYYY_AT_HH_MM),
                windowStart = formatDate(windowStartDateTime, DateFormatConstants.DD_MMMM_YYYY_AT_HH_MM),
                windowStartTime = formatDate(windowStartDateTime, DateFormatConstants.HH_MM),
                windowEndTime = formatDate(windowEndDateTime, DateFormatConstants.HH_MM),
                windowDuration = duration,
                windowStartDateTime = windowStartDateTime,
                windowEndDateTime = windowEndDateTime,
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

    private fun calculateDuration(startTime: LocalDateTime?, endTime: LocalDateTime?): String? {
        if (startTime == null || endTime == null) return null

        // If times are the same, it's an instantaneous launch
        if (startTime == endTime) return "Instantaneous"

        val duration = java.time.Duration.between(startTime, endTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        return buildString {
            if (hours > 0) {
                append("${hours}h")
                if (minutes > 0 || seconds > 0) append(" ")
            }
            if (minutes > 0) {
                append("${minutes}m")
                if (seconds > 0) append(" ")
            }
            if (seconds > 0 || (hours == 0L && minutes == 0L)) {
                append("${seconds}s")
            }
        }.trim()
    }

    fun mapToLaunchesUi(launch: LaunchSummary): LaunchesUi {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            return LaunchesUi(
                id = id,
                launchDate = formatDate(locateDateTime),
                missionName = missionName,
                status = status.toDomain(),
                thumbnail = thumbnailUrl
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