package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company

internal interface CompanyRemoteDataSource {

    suspend fun getCompanyApi(): LaunchResult<Company, DataError>
}
