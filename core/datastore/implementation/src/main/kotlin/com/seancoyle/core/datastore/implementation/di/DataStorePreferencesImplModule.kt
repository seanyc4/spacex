package com.seancoyle.core.datastore.implementation.di

import com.seancoyle.core.datastore.implementation.data.DataStorePreferencesImpl
import com.seancoyle.core.datastore.implementation.domain.DataStorePreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStorePreferencesImplModule {

    @Binds
    abstract fun bindsDataStorePreferences(
        impl: DataStorePreferencesImpl
    ): DataStorePreferences
}