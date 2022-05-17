package com.seancoyle.spacex.business.data.network.launch

import com.google.gson.GsonBuilder
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeLaunchNetworkDataSourceImpl
constructor(
    private val baseUrl: HttpUrl,
    private val networkMapper: LaunchNetworkMapper
) : LaunchNetworkDataSource {

    private val api: LaunchApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(LaunchApi::class.java)

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<LaunchModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}
