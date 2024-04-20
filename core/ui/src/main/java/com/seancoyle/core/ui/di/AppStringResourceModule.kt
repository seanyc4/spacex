package com.seancoyle.core.ui.di

import com.seancoyle.core.domain.AppStringResource
import com.seancoyle.core.ui.AppStringResourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppStringResourceModule {
    @Binds
    abstract fun bindAppStringResource(
        impl: AppStringResourceImpl
    ): AppStringResource
}