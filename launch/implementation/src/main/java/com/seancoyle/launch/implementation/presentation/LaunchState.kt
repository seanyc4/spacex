package com.seancoyle.launch.implementation.presentation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.seancoyle.launch.api.LaunchNetworkConstants.ORDER_ASC
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.parcelize.Parcelize

@Immutable
data class LaunchState(
    val mergedLaunches: List<ViewType> = emptyList(),
    val isLoading: Boolean = false
)

@Parcelize
@Immutable
data class FilterState(
    val isDialogFilterDisplayed: Boolean = false,
    val launchFilter: Int? = null,
    val order: String = ORDER_ASC,
    val year: String = "",
): Parcelable

@Parcelize
@Immutable
data class ListState(
    val page: Int = 1,
    val scrollPosition: Int = 0
): Parcelable