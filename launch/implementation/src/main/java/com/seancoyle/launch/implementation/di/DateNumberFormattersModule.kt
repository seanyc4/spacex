package com.seancoyle.launch.implementation.di

import com.seancoyle.core.domain.DateFormatConstants
import com.seancoyle.core.domain.DateFormatter
import com.seancoyle.core.domain.DateTransformer
import com.seancoyle.core.domain.NumberFormatter
import com.seancoyle.launch.implementation.DateFormatterImpl
import com.seancoyle.launch.implementation.DateTransformerImpl
import com.seancoyle.launch.implementation.NumberFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DateNumberFormattersModule {

    @Binds
    abstract fun provideDateFormatter(
        impl: DateFormatterImpl
    ): DateFormatter

    @Binds
    abstract fun provideNumberFormatter(
        impl: NumberFormatterImpl
    ): NumberFormatter

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