package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
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
    ): Flow<DataResult<List<ViewType>?, DataError>>

    fun createMergedLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>, DataError>>

    fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<Launch>, DataError>>

    fun getCompanyCacheUseCase(): Flow<DataResult<Company?, DataError>>

    fun getCompanyApiAndCacheUseCase(): Flow<DataResult<Company, DataError>>

}