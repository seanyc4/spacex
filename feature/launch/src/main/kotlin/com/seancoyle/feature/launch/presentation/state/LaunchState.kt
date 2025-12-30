package com.seancoyle.feature.launch.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.feature.launch.domain.model.LaunchStatus
import com.seancoyle.feature.launch.domain.model.LaunchType
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
data class LaunchesScreenState(
    val query: String = "",
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val launchType: LaunchType = LaunchType.UPCOMING,
    val isFilterDialogVisible: Boolean = false,
    val scrollPosition: Int = 0,
    val isRefreshing: Boolean = false,
) : Parcelable
