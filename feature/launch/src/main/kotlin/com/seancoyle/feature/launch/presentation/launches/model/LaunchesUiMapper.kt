package com.seancoyle.feature.launch.presentation.launches.model

import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.feature.launch.domain.model.Launch
import java.time.LocalDateTime
import javax.inject.Inject

class LaunchesUiMapper @Inject constructor(
    private val dateFormatter: DateTransformer
) {

    operator fun invoke(launch: Launch): LaunchesUi {
        with(launch) {
            val locateDateTime = dateFormatter.formatDate(net)
            return LaunchesUi(
                id = id,
                launchDate = formatDate(locateDateTime),
                missionName = name.orEmpty(),
                status = status,
                image = image.thumbnailUrl
            )
        }
    }

    private fun formatDate(date: LocalDateTime): String {
        return dateFormatter.formatDateTimeToString(date)
    }
}
