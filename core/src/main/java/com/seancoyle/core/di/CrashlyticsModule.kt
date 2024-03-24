package com.seancoyle.core.di

import com.seancoyle.core.util.Crashlytics
import com.seancoyle.core.util.CrashlyticsImpl
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