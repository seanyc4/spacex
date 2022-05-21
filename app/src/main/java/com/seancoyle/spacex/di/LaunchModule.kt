package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.implementation.launch.LaunchCacheDataSourceImpl
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.data.network.implementation.launch.LaunchNetworkDataSourceImpl
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.launch.LaunchDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchEntityMapper
import com.seancoyle.spacex.framework.datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import com.seancoyle.spacex.framework.datasource.network.implementation.launch.LaunchRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.model.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

const val LAUNCH_OPTIONS_ROCKET = "rocket"
const val LAUNCH_OPTIONS_SORT = "desc"

@Module
@InstallIn(SingletonComponent::class)
object LaunchModule {

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
        api: LaunchApi,
        networkMapper: LaunchNetworkMapper
    ): LaunchRetrofitService {
        return LaunchRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideLaunchApi(
        retrofit: Retrofit
    ): LaunchApi {
        return retrofit.create(LaunchApi::class.java)
    }

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
            entityMapper = launchEntityMapper
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

    @Singleton
    @Provides
    fun provideLaunchOptions(): LaunchOptions {
        return LaunchOptions(
            options = Options(
                populate = listOf(
                    Populate(
                        path = LAUNCH_OPTIONS_ROCKET,
                        select = Select(
                            name = 1,
                            type =2
                        )
                    )
                ),
                sort = Sort(
                    flight_number = LAUNCH_OPTIONS_SORT,
                ),
                limit =500
            )
        )
    }

}