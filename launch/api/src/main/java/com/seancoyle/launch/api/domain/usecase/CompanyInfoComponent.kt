package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

interface CompanyInfoComponent {
    fun getCompanyInfoFromCacheUseCase(): Flow<DataResult<Company?>>
    fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): Flow<DataResult<Company>>
}