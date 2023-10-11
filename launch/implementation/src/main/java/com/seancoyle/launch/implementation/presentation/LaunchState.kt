package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchState(
    @IgnoredOnParcel
    val mergedLaunches: List<ViewType> = emptyList(),
    val scrollPosition: Int = 0,
    val isLoading: Boolean = false,
    val isDialogFilterDisplayed: Boolean = false,
    val launchFilter: Int? = null,
    val order: String = ORDER_ASC,
    val year: String = "",
    val page: Int = 1
): Parcelable