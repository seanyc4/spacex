package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.implementation.company.CompanyInfoCacheDataSourceImpl
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.implementation.company.CompanyInfoNetworkDataSourceImpl
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.company.CompanyInfoDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoEntityMapper
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.implementation.company.CompanyInfoRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoModule {

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkDataSource(
        companyInfoRetrofitService: CompanyInfoRetrofitService
    ): CompanyInfoNetworkDataSource {
        return CompanyInfoNetworkDataSourceImpl(
            retrofitService = companyInfoRetrofitService
        )
    }

    @Singleton
    @Provides
    fun provideCompanyInfoRetrofitService(
        api: CompanyInfoApi,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoRetrofitService {
        return CompanyInfoRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideCompanyInfoApi(
        retrofit: Retrofit
    ): CompanyInfoApi {
        return retrofit.create(CompanyInfoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkMapper(
        numberFormatter: NumberFormatter
    ): CompanyInfoNetworkMapper {
        return CompanyInfoNetworkMapper(
            numberFormatter = numberFormatter
        )
    }

    @Singleton
    @Provides
    fun provideCompanyInfoFactory(): CompanyInfoFactory {
        return CompanyInfoFactory()
    }

    @Singleton
    @Provides
    fun provideCompanyInfoDao(database: Database): CompanyInfoDao {
        return database.companyInfoDao()
    }

    @Singleton
    @Provides
    fun provideCompanyInfoCacheMapper(): CompanyInfoEntityMapper {
        return CompanyInfoEntityMapper()
    }

    @Singleton
    @Provides
    fun provideCompanyInfoDaoService(
        dao: CompanyInfoDao,
        companyInfoEntityMapper: CompanyInfoEntityMapper
    ): CompanyInfoDaoService {
        return CompanyInfoDaoServiceImpl(
            dao = dao,
            entityMapper = companyInfoEntityMapper
        )
    }

    @Singleton
    @Provides
    fun provideCompanyInfoCacheDataSource(
        daoService: CompanyInfoDaoService
    ): CompanyInfoCacheDataSource {
        return CompanyInfoCacheDataSourceImpl(
            daoService = daoService
        )
    }

}