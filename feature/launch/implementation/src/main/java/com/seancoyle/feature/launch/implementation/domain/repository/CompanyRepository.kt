package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company

interface CompanyRepository {
    suspend fun getCompany(): Result<Company, DataError>
    suspend fun insertCompany(company: Company): Result<Long, DataError>
    suspend fun getCompanyFromCache(): Result<Company?, DataError>
    suspend fun deleteAllCompanyCache(): Result<Unit, DataError>
}