package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.network.CompanyNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CompanyNetworkDataSourceImpl @Inject constructor(
    private val api: CompanyApiService,
    private val networkMapper: CompanyNetworkMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : CompanyNetworkDataSource {
    override suspend fun getCompany(): Result<Company, DataError> {
        return safeApiCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            networkMapper.mapFromEntity(api.getCompany())
        }
    }
}