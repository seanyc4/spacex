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
        val mergedLaunches: List<ViewType>? = emptyList(),
        @IgnoredOnParcel
        val launches: List<Launch>? = emptyList(),
        val company: CompanyInfo? = null,
        val numLaunchesInCache: Int? = 0,
        val page: Int? = 1,
        val isDialogFilterDisplayed: Boolean? = false,
        val launchFilter: Int? = null,
        val order: String? = null,
        val yearQuery: String? = "",
        val scrollPosition: Int? = 0,
        val loading :Boolean = false

        ) : LaunchUiState(), Parcelable

}