package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

interface CompanyInfoComponent {
    fun getCompanyInfoFromCacheUseCase(): Flow<Company?>
    fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): Flow<ApiResult<Company>>
}