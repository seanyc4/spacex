package com.seancoyle.launch.api.model

import android.os.Parcelable
import com.seancoyle.core.state.ViewState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchViewState(

    @IgnoredOnParcel
    var mergedList: List<LaunchType>? = emptyList(),
    @IgnoredOnParcel
    var launchList: List<LaunchModel>? = emptyList(),
    var launch: LaunchModel? = null,
    var company: CompanyInfoModel? = null,
    var numLaunchItemsInCache: Int? = 0,
    var page: Int? = 1,
    var isDialogFilterDisplayed: Boolean? = false,
    var launchFilter: Int? = null,
    var order: String? = null,
    var yearQuery: String? = "",
    var scrollPosition: Int? = 0

) : Parcelable, ViewState
























