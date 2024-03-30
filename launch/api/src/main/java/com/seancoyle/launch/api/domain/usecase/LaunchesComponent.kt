package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {

    fun sortAndFilterLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>>

    fun createMergedLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>>>

    fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<Launch>>>

    fun getCompanyCacheUseCase(): Flow<DataResult<Company?>>

    fun getCompanyApiAndCacheUseCase(): Flow<DataResult<Company>>

}