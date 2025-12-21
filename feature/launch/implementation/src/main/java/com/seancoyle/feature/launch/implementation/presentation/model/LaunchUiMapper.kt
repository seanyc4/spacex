package com.seancoyle.feature.launch.implementation.presentation.model

import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.presentation.getDateStringRes
import com.seancoyle.feature.launch.implementation.presentation.getDrawableRes
import javax.inject.Inject

internal class LaunchUiMapper @Inject constructor(
    private val appStringResource: AppStringResource,
    private val dateFormatter: DateTransformer
) {

    operator fun invoke(launch: LaunchTypes.Launch): LaunchUi {
        with(launch) {
            return LaunchUi(
                id = id,
                launchDate = formatDate(net),
                launchYear = "",
                launchStatus = launchStatus,
                missionName = "",
                launchDateStatus = LaunchDateStatus.FUTURE,
                launchDays = "",
                launchDaysResId = LaunchDateStatus.FUTURE.getDateStringRes(),
                launchStatusIconResId = launchStatus.getDrawableRes(),
                image = image?.thumbnailUrl.orEmpty()
            )
        }
    }

    private fun formatDate(net: String?): String {
        net ?: return ""
        val locateDateTime = dateFormatter.formatDate(net)
        return dateFormatter.formatDateTimeToString(locateDateTime)
    }
}