package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import javax.inject.Inject

class GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyInfoCacheDataSource,
    private val networkDataSource: CompanyInfoNetworkDataSource
) : GetCompanyInfoFromNetworkAndInsertToCacheUseCase {

    override suspend operator fun invoke() {
       val result = networkDataSource.getCompanyInfo()

        result.let {
            cacheDataSource.insert(result)
        }
    }

}