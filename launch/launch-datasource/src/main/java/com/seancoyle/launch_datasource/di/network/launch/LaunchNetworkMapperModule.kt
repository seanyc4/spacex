package com.seancoyle.launch_datasource.di.network.launch

import com.seancoyle.launch_datasource.network.abstraction.datetransformer.DateTransformer
import com.seancoyle.launch_datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.launch_datasource.network.mappers.launch.LaunchNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchNetworkMapperModule {

    @Singleton
    @Provides
    fun provideLaunchNetworkMapper(
        dateFormatter: DateFormatter,
        dateTransformer: DateTransformer
    ): LaunchNetworkMapper {
        return LaunchNetworkMapper(
            dateFormatter = dateFormatter,
            dateTransformer = dateTransformer
        )
    }
}