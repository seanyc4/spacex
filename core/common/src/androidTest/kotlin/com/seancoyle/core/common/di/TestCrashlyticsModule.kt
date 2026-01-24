package com.seancoyle.core.common.di

import com.seancoyle.core.common.crashlytics.CrashLogger
import com.seancoyle.core.common.crashlytics.FakeCrashlyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CrashlyticsModule::class])
abstract class TestCrashlyticsModule {

    @Binds
    abstract fun bindCrashLogger(
        impl: FakeCrashlyticsImpl
    ): CrashLogger
}
