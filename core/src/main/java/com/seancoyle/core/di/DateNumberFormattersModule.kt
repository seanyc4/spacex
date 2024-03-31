package com.seancoyle.core.di

import com.seancoyle.core.domain.DateFormatter
import com.seancoyle.core.domain.DateTransformer
import com.seancoyle.core.domain.NumberFormatter
import com.seancoyle.core.presentation.DateFormatConstants
import com.seancoyle.core.presentation.DateFormatterImpl
import com.seancoyle.core.presentation.DateTransformerImpl
import com.seancoyle.core.presentation.NumberFormatterImpl
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