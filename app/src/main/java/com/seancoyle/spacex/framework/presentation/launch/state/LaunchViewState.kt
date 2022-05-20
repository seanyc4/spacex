package com.seancoyle.spacex.framework.presentation.launch.state

import android.os.Parcelable
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.domain.state.ViewState
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_ALL
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
    var launchFilter: Int? = LAUNCH_ALL,
    var filter: String? = null,
    var order: String? = LAUNCH_ORDER_DESC,
    var searchQuery: String? = null,

    ): Parcelable, ViewState
























