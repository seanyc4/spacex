package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class LaunchState(
    @IgnoredOnParcel
    val mergedLaunches: List<ViewType> = emptyList(),
    val isLoading: Boolean = false,
    val isDialogFilterDisplayed: Boolean = false,
    val launchFilter: Int? = null,
    val order: String = ORDER_ASC,
    val year: String = "",
    val page: Int = 1,
    val scrollPosition: Int = 0
): Parcelable