package com.seancoyle.feature.launch.presentation.launches.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class LaunchesUiState(
    val query: String = "",
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val launchesType: LaunchesType = LaunchesType.UPCOMING,
    val isFilterBottomSheetVisible: Boolean = false,
    val upcomingScrollPosition: Int = 0,
    val pastScrollPosition: Int = 0,
    val isRefreshing: Boolean = false,
) : Parcelable