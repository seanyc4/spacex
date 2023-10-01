package com.seancoyle.launch.api.presentation

import android.os.Parcelable
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

sealed class LaunchUiState {

  /*  object Loading : LaunchUiState()

    object Empty : LaunchUiState()
*/
    @Parcelize
    data class LaunchState(

        @IgnoredOnParcel
        var mergedLaunches: List<ViewType>? = emptyList(),
        @IgnoredOnParcel
        var launches: List<Launch>? = emptyList(),
        var company: CompanyInfo? = null,
        var numLaunchesInCache: Int? = 0,
        var page: Int? = 1,
        var isDialogFilterDisplayed: Boolean? = false,
        var launchFilter: Int? = null,
        var order: String? = null,
        var yearQuery: String? = "",
        var scrollPosition: Int? = 0,
        var loading :Boolean = false

        ) : LaunchUiState(), Parcelable

}