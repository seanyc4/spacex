package com.seancoyle.launch_viewstate

import android.os.Parcelable
import com.seancoyle.core.state.ViewState
import com.seancoyle.launch_domain.model.company.CompanyInfoModel
import com.seancoyle.launch_domain.model.launch.LaunchModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchViewState(

    var launchList: List<LaunchModel>? = null,
    var launch: LaunchModel? = null,
    var company: CompanyInfoModel? = null,
    var layoutManagerState: Parcelable? = null,
    var numLaunchItemsInCache: Int? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var isDialogFilterDisplayed: Boolean? = null,
    var launchFilter: Int? = null,
    var order: String? = null,
    var yearQuery: String? = null,

    ): Parcelable, ViewState
























