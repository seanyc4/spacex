package com.seancoyle.spacex.di.data.cache.companyinfo

import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.company.CompanyInfoDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoCacheDaoServiceModule {

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

}