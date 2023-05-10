package com.seancoyle.spacex.di

import com.seancoyle.core.presentation.util.DateFormatConstants
import com.seancoyle.core.presentation.util.DateFormatter
import com.seancoyle.core.presentation.util.DateTransformer
import com.seancoyle.core.presentation.util.NumberFormatter
import com.seancoyle.core.util.*
import com.seancoyle.spacex.util.DateFormatterImpl
import com.seancoyle.spacex.util.DateTransformerImpl
import com.seancoyle.spacex.util.NumberFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DateUtilModule {

    @Singleton
    @Binds
    abstract fun provideDateFormatter(
        impl: DateFormatterImpl
    ): DateFormatter

    @Singleton
    @Binds
    abstract fun provideNumberFormatter(
        impl: NumberFormatterImpl
    ): NumberFormatter

    @Singleton
    @Binds
    abstract fun provideDateTransformer(
        impl: DateTransformerImpl
    ): DateTransformer

    companion object {
        @Singleton
        @Provides
        fun provideDateFormat(): DateTimeFormatter {
            return DateTimeFormatter.ofPattern(
                DateFormatConstants.YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH
            ).withZone(
                ZoneId.systemDefault()
            )
        }
    }
}