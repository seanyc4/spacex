package com.seancoyle.core_datastore

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