package com.seancoyle.feature.launch.presentation.model

import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.domain.model.LaunchTypes
import java.time.LocalDateTime
import javax.inject.Inject

internal class LaunchUiMapper @Inject constructor(
    private val dateFormatter: DateTransformer
) {

    operator fun invoke(launch: LaunchTypes.Launch): LaunchUi {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            return LaunchUi(
                id = id,
                launchDate = formatDate(locateDateTime),
                launchStatus = launchStatus,
                missionName = name.orEmpty(),
                launchDays = dateFormatter.getLaunchDaysDifference(locateDateTime),
                launchDaysResId = LaunchDateStatus.FUTURE.getDateStringRes(),
                launchStatusIconResId = launchStatus.getDrawableRes(),
                image = image.thumbnailUrl
            )
        }
    }

    private fun formatDate(date: LocalDateTime): String {
        return dateFormatter.formatDateTimeToString(date)
    }
}
