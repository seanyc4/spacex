package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

internal interface GetCompanyCacheUseCase {
    operator fun invoke(): Flow<Result<Company?, DataError>>
}