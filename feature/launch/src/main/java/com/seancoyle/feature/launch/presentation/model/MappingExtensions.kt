package com.seancoyle.feature.launch.presentation.model

import com.seancoyle.feature.launch.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.R

fun LaunchStatus.getDrawableRes(): Int = when (this) {
    LaunchStatus.SUCCESS -> R.drawable.ic_launch_success
    LaunchStatus.FAILED -> R.drawable.ic_launch_fail
    LaunchStatus.UNKNOWN -> R.drawable.ic_launch_unknown
    LaunchStatus.ALL -> throw IllegalArgumentException("LaunchStatus.ALL is not supported here")
}

fun LaunchDateStatus.getDateStringRes(): Int = when (this) {
    LaunchDateStatus.PAST -> R.string.days_since_now
    LaunchDateStatus.FUTURE -> R.string.days_from_now
}
