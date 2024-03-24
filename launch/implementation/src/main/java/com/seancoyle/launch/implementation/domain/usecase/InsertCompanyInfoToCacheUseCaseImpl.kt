package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class InsertCompanyInfoToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : InsertCompanyInfoToCacheUseCase {

    override suspend operator fun invoke(companyInfo: Company): DataResult<Long?> =
        safeCacheCall(ioDispatcher) {
            cacheDataSource.insert(companyInfo)
        }
}