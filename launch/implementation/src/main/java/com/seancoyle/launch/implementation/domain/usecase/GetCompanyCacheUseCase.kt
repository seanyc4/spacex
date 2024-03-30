package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

internal interface GetCompanyCacheUseCase {
    operator fun invoke(): Flow<DataResult<Company?>>
}