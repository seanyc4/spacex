package com.seancoyle.launch.implementation.data.network

import com.google.gson.GsonBuilder
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.api.domain.model.ViewModel
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

    override suspend fun getLaunchList(launchOptions: LaunchOptions): List<ViewModel> {
        return networkMapper.mapEntityToList(
            api.getLaunchList(options = launchOptions)
        )
    }

}
