package com.seancoyle.spacex.di

import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.core_datastore.AppDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object TestDataStoreModule {

    @Singleton
    @Provides
    fun provideDataStoreManager(
        application: HiltTestApplication
    ): AppDataStore {
        return AppDataStoreManager(application)
    }
}