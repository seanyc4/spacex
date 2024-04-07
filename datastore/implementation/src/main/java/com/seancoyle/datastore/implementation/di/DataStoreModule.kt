package com.seancoyle.datastore.implementation.di

import com.seancoyle.datastore.implementation.data.AppDataStoreImpl
import com.seancoyle.datastore.implementation.domain.AppDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStoreModule {

    @Singleton
    @Binds
    abstract fun bindsDataStoreManager(
        impl: AppDataStoreImpl
    ): AppDataStore
}