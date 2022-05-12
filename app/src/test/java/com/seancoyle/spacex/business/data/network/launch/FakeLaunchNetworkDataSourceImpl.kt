package com.seancoyle.spacex.business.data.network.launch

import com.google.gson.GsonBuilder
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchService
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeLaunchNetworkDataSourceImpl
constructor(
    private val baseUrl: HttpUrl,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    private val service: LaunchService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(LaunchService::class.java)


    override suspend fun getLaunchList(): List<LaunchDomainEntity> {
        return networkMapper.entityListToDomainList(
            service.getLaunchList()
        )
    }

}
