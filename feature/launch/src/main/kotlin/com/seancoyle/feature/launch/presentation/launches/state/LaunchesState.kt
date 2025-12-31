package com.seancoyle.feature.launch.presentation.launches.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.LaunchStatus
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class LaunchesState(
    val query: String = "",
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val launchesType: LaunchesType = LaunchesType.UPCOMING,
    val isFilterDialogVisible: Boolean = false,
    val scrollPosition: Int = 0,
    val isRefreshing: Boolean = false,
) : Parcelable
