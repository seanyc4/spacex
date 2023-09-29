package com.seancoyle.launch.api.domain.model

import android.os.Parcelable
import com.seancoyle.core.domain.UiState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

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

) : Parcelable, UiState
























