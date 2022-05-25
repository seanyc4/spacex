package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
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