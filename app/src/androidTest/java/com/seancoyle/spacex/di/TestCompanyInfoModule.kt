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
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoCacheMapper
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoService
import com.seancoyle.spacex.framework.datasource.network.implementation.company.CompanyInfoRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CompanyInfoModule::class]
)
object TestCompanyInfoModule {

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
        service: CompanyInfoService,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoRetrofitService {
        return CompanyInfoRetrofitServiceImpl(
            service = service,
            networkMapper = networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideCompanyInfoApi(
        retrofit: Retrofit
    ): CompanyInfoService {
        return retrofit.create(CompanyInfoService::class.java)
    }

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkMapper(): CompanyInfoNetworkMapper {
        return CompanyInfoNetworkMapper()
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
    fun provideCompanyInfoCacheMapper(): CompanyInfoCacheMapper {
        return CompanyInfoCacheMapper()
    }

    @Singleton
    @Provides
    fun provideCompanyInfoDaoService(
        dao: CompanyInfoDao,
        companyInfoCacheMapper: CompanyInfoCacheMapper
    ): CompanyInfoDaoService {
        return CompanyInfoDaoServiceImpl(
            dao = dao,
            cacheMapper = companyInfoCacheMapper
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