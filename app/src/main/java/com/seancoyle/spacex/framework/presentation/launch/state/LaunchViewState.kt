package com.seancoyle.spacex.framework.presentation.launch.state

import android.os.Parcelable
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.state.ViewState
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchViewState(

    var launchList: List<LaunchDomainEntity>? = null,
    var launch: LaunchDomainEntity? = null,
    var company: CompanyInfoDomainEntity? = null,
    var layoutManagerState: Parcelable? = null,
    var numLaunchItemsInCache: Int? = null,
    var page: Int? = null,
    var isQueryExhausted: Boolean? = null,
    var launchFilter: Int? = null,
    var filter: String? = null,
    var order: String? = null,
    var searchQuery: String? = null,

    ): Parcelable, ViewState
























