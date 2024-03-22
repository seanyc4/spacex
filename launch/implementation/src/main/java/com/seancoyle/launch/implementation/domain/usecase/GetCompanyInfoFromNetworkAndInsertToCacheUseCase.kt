package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

internal interface GetCompanyInfoFromNetworkAndInsertToCacheUseCase {
    operator fun invoke(): Flow<ApiResult<Company>>
}