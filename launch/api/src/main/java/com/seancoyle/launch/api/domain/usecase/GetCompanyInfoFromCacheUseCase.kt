package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

interface GetCompanyInfoFromCacheUseCase {
    operator fun invoke(): Flow<Company?>
}