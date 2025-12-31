package com.seancoyle.feature.launch.presentation.launch

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.core.domain.LaunchesType
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class LaunchScreenState(
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val launchesType: LaunchesType = LaunchesType.UPCOMING,
    val scrollPosition: Int = 0,
) : Parcelable
