package com.seancoyle.launch.implementation.domain

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.core.data.network.safeApiCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.data.CompanyCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource,
    private val networkDataSource: CompanyInfoNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetCompanyInfoFromNetworkAndInsertToCacheUseCase {

    override operator fun invoke(): Flow<ApiResult<Company>> = flow {
       val result = safeApiCall(ioDispatcher){
           networkDataSource.getCompany()
       }

        when (result) {
            is ApiResult.Success -> {
                result.data?.let { companyInfo ->
                    cacheDataSource.insert(companyInfo)
                    emit(ApiResult.Success(companyInfo))
                }
            }
            is ApiResult.Error -> {
                emit(result)
            }

            else -> {}
        }
    }
}