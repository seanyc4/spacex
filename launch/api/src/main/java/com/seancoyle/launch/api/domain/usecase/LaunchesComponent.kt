package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {

    fun filterLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>>

    fun createMergeAndFilteredLaunchesUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>>>

    fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<Launch>>>

    fun getCompanyInfoFromCacheUseCase(): Flow<DataResult<Company?>>

    fun getCompanyInfoApiAndCacheUseCase(): Flow<DataResult<Company>>

}