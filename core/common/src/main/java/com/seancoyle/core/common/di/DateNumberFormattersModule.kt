package com.seancoyle.core.common.di

import com.seancoyle.core.common.dataformatter.DateFormatConstants
import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateFormatterImpl
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.dataformatter.DateTransformerImpl
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