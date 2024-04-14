package com.seancoyle.launch.implementation.data.network

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CompanyInfoNetworkDataSourceImpl @Inject constructor(
    private val api: CompanyApi,
    private val networkMapper: CompanyInfoNetworkMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyInfoNetworkDataSource {
    override suspend fun getCompany(): DataResult<Company, DataError> {
        return safeApiCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            networkMapper.mapFromEntity(api.getCompany())
        }
    }
}