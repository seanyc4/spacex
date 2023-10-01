package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.CompanyInfo
import kotlinx.coroutines.flow.Flow

interface GetCompanyInfoFromCacheUseCase {
    suspend operator fun invoke(): Flow<CompanyInfo>
}