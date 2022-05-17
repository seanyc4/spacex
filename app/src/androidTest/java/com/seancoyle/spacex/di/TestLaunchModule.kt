package com.seancoyle.spacex.di

import com.google.gson.GsonBuilder
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.implementation.launch.LaunchCacheDataSourceImpl
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.data.network.implementation.launch.LaunchNetworkDataSourceImpl
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.spacex.framework.datasource.cache.implementation.launch.LaunchDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchEntityMapper
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchService
import com.seancoyle.spacex.framework.datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.implementation.launch.LaunchRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchModule::class]
)
object TestLaunchModule {

    @Singleton
    @Provides
    fun provideLaunchNetworkDataSource(
        launchRetrofitService: LaunchRetrofitService
    ): LaunchNetworkDataSource {
        return LaunchNetworkDataSourceImpl(
            retrofitService = launchRetrofitService
        )
    }

    @Singleton
    @Provides
    fun provideLaunchRetrofitService(
        service: LaunchService,
        networkMapper: LaunchNetworkMapper
    ): LaunchRetrofitService {
        return LaunchRetrofitServiceImpl(
            service = service,
            networkMapper = networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideLaunchApi(
        retrofit: Retrofit
    ): LaunchService {
        return retrofit.create(LaunchService::class.java)
    }

    @Singleton
    @Provides
    fun provideLaunchRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Singleton
    @Provides
    fun provideLaunchNetworkMapper(
        dateFormatter: DateFormatterImpl,
        dateTransformer: DateTransformerImpl
    ): LaunchNetworkMapper {
        return LaunchNetworkMapper(
            dateFormatter = dateFormatter,
            dateTransformer = dateTransformer
        )
    }

    @Singleton
    @Provides
    fun provideLaunchFactory(): LaunchFactory {
        return LaunchFactory()
    }

    @Singleton
    @Provides
    fun provideLaunchDao(database: Database): LaunchDao {
        return database.launchDao()
    }

    @Singleton
    @Provides
    fun provideLaunchCacheMapper(): LaunchEntityMapper {
        return LaunchEntityMapper()
    }

    @Singleton
    @Provides
    fun provideLaunchDaoService(
        dao: LaunchDao,
        launchEntityMapper: LaunchEntityMapper
    ): LaunchDaoService {
        return LaunchDaoServiceImpl(
            dao = dao,
            cacheMapper = launchEntityMapper
        )
    }

    @Singleton
    @Provides
    fun provideLaunchCacheDataSource(
        daoService: LaunchDaoService
    ): LaunchCacheDataSource {
        return LaunchCacheDataSourceImpl(
            daoService = daoService
        )
    }

}