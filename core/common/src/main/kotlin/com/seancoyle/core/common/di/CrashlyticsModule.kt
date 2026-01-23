package com.seancoyle.core.common.di

import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.core.common.crashlytics.CrashlyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CrashlyticsModule {
    @Binds
    abstract fun bindCrashlytics(crashlytics: CrashlyticsImpl): CrashLogger
}