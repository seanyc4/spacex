package com.seancoyle.feature.launch.implementation.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.api.LaunchConstants.INITIAL_LOAD_SIZE
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.api.LaunchConstants.PREFETCH_DISTANCE
import com.seancoyle.feature.launch.implementation.data.remote.LaunchRemoteMediator
import com.seancoyle.feature.launch.implementation.data.repository.LaunchPreferencesRepositoryImpl
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRepositoryImpl
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindLaunchPreferencesRepository(
        impl: LaunchPreferencesRepositoryImpl
    ): LaunchPreferencesRepository

    @Binds
    abstract fun bindLaunchRepository(
        impl: LaunchRepositoryImpl
    ): LaunchRepository

    companion object {
        @OptIn(ExperimentalPagingApi::class)
        @Provides
        @Singleton
        fun provideLaunchPager(
            launchDao: LaunchDao,
            remoteMediator: LaunchRemoteMediator,
        ): Pager<Int, LaunchEntity> {
            return Pager(
                config = PagingConfig(
                    pageSize = PAGINATION_LIMIT,
                    enablePlaceholders = false,
                    initialLoadSize = INITIAL_LOAD_SIZE,
                    prefetchDistance = PREFETCH_DISTANCE,
                ),
                remoteMediator = remoteMediator,
                pagingSourceFactory = {
                    launchDao.pagingSource()
                }
            )
        }
    }

}
