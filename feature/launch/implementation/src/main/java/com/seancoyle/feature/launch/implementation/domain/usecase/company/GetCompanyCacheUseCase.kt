package com.seancoyle.feature.launch.implementation.domain.usecase.company

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import kotlinx.coroutines.flow.Flow

internal interface GetCompanyCacheUseCase {
    operator fun invoke(): Flow<LaunchResult<Company?, LocalError>>
}