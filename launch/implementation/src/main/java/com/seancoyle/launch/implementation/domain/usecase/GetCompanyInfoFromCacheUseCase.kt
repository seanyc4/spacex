package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

internal interface GetCompanyInfoFromCacheUseCase {
    operator fun invoke(): Flow<Company?>
}