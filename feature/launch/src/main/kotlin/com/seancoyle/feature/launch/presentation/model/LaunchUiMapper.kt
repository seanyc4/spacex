package com.seancoyle.feature.launch.presentation.model

import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.domain.model.Launch
import java.time.LocalDateTime
import javax.inject.Inject

class LaunchUiMapper @Inject constructor(
    private val dateFormatter: DateTransformer
) {

    operator fun invoke(launch: Launch): LaunchUi {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            return LaunchUi(
                id = id,
                launchDate = formatDate(locateDateTime),
                launchStatus = launchStatus,
                missionName = name.orEmpty(),
                launchDays = dateFormatter.getLaunchDaysDifference(locateDateTime),
                image = image.thumbnailUrl
            )
        }
    }

    private fun formatDate(date: LocalDateTime): String {
        return dateFormatter.formatDateTimeToString(date)
    }
}
