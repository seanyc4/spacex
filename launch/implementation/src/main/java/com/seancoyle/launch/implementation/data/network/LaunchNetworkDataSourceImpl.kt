package com.seancoyle.launch.implementation.data.network

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.util.Crashlytics
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchNetworkDataSourceImpl @Inject constructor(
    private val api: LaunchApi,
    private val networkMapper: LaunchNetworkMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchNetworkDataSource {
    override suspend fun getLaunches(launchOptions: LaunchOptions): DataResult<List<Launch>> {
        return safeApiCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            networkMapper.mapEntityToList(api.getLaunches(launchOptions))
        }
    }
}