package com.seancoyle.core.datastore.implementation.di

import com.seancoyle.core.datastore.api.DataStorePreferencesComponent
import com.seancoyle.core.datastore.implementation.domain.DataStorePreferencesComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataStorePreferencesComponentModule {

    @Binds
    abstract fun bindsDataStorePreferencesComponent(
        impl: DataStorePreferencesComponentImpl
    ): DataStorePreferencesComponent
}