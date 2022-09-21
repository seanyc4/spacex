package com.seancoyle.spacex.di

import com.seancoyle.core.util.DateFormatConstants.YYYY_MM_DD_HH_MM_SS
import com.seancoyle.launch_datasource.network.abstraction.datetransformer.DateTransformer
import com.seancoyle.launch_datasource.network.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.launch_datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.launch_datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.launch_datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.launch_datasource.network.implementation.numberformatter.NumberFormatterImpl
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
object AppModule {


    @Singleton
    @Provides
    fun provideDateFormat(): DateTimeFormatter {
        return DateTimeFormatter.ofPattern(
            YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH
        ).withZone(
            ZoneId.systemDefault()
        )
    }

    @Singleton
    @Provides
    fun provideDateFormatter(
        dateFormat: DateTimeFormatter
    ): DateFormatter {
        return DateFormatterImpl(
            dateFormat = dateFormat
        )
    }

    @Singleton
    @Provides
    fun provideNumberFormatter(): NumberFormatter {
        return NumberFormatterImpl()
    }

    @Singleton
    @Provides
    fun provideDateTransformer(): DateTransformer {
        return DateTransformerImpl()
    }

}











