package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource
) : GetCompanyCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Company?, DataError>> = flow {
        emit(cacheDataSource.getCompany())
    }
}