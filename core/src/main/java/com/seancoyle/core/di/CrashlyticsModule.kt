package com.seancoyle.core.di

import com.seancoyle.core.data.CrashlyticsImpl
import com.seancoyle.core.domain.Crashlytics
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashlyticsModule {

    @Singleton
    @Binds
    abstract fun bindCrashlytics(crashlytics: CrashlyticsImpl): Crashlytics
}