package com.seancoyle.launch.api.model

import android.os.Parcelable
import com.seancoyle.core.state.UiState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchState(

    @IgnoredOnParcel
    var mergedLaunches: List<LaunchType>? = emptyList(),
    @IgnoredOnParcel
    var launches: List<LaunchModel>? = emptyList(),
    var company: CompanyInfoModel? = null,
    var numLaunchesInCache: Int? = 0,
    var page: Int? = 1,
    var isDialogFilterDisplayed: Boolean? = false,
    var launchFilter: Int? = null,
    var order: String? = null,
    var yearQuery: String? = "",
    var scrollPosition: Int? = 0,
    var isRefreshing: Boolean = false

) : Parcelable, UiState
























