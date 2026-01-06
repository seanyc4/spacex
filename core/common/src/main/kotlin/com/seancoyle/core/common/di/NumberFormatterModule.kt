package com.seancoyle.core.common.di

import com.seancoyle.core.common.numberformatter.NumberFormatter
import com.seancoyle.core.common.numberformatter.NumberFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NumberFormatterModule {
    @Binds
    abstract fun provideNumberFormatter(
        impl: NumberFormatterImpl
    ): NumberFormatter
}