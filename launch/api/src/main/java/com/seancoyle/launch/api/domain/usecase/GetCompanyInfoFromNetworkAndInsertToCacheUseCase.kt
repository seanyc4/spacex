package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.CompanyInfo
import kotlinx.coroutines.flow.Flow

interface GetCompanyInfoFromNetworkAndInsertToCacheUseCase {
    operator fun invoke(): Flow<ApiResult<CompanyInfo>>
}