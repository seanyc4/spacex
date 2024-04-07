package com.seancoyle.datastore.implementation.di

import com.seancoyle.datastore.api.AppDataStoreComponent
import com.seancoyle.datastore.implementation.domain.AppDataStoreComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
@Module
@InstallIn(ViewModelComponent::class)
internal abstract class DataStoreComponentModule {

    @Binds
    abstract fun bindsAppDataStoreComponent(
        impl: AppDataStoreComponentImpl
    ): AppDataStoreComponent
}