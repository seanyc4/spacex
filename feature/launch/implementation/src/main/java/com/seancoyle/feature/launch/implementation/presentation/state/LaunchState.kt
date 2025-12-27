package com.seancoyle.feature.launch.implementation.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import kotlinx.parcelize.Parcelize

@Parcelize
@Stable
internal data class LaunchesScreenState(
    val query: String = "",
    val order: Order = Order.ASC,
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val isFilterDialogVisible: Boolean = false,
    val scrollPosition: Int = 0,
    val isRefreshing: Boolean = false,
) : Parcelable
