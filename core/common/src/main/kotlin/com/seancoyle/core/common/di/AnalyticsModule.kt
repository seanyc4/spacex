package com.seancoyle.core.common.di

import com.seancoyle.core.common.analytics.AnalyticsLogger
import com.seancoyle.core.common.analytics.FirebaseAnalyticsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalytics(
        impl: FirebaseAnalyticsImpl
    ): AnalyticsLogger
}
