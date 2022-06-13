package com.seancoyle.spacex.di

import com.google.gson.GsonBuilder
import com.seancoyle.spacex.BuildConfig
import com.seancoyle.launch_datasource.network.NetworkConstants.NETWORK_TIMEOUT
import com.seancoyle.launch_datasource.network.abstraction.datetransformer.DateTransformer
import com.seancoyle.launch_datasource.network.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.launch_datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.launch_datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.launch_datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.launch_datasource.network.implementation.numberformatter.NumberFormatterImpl
import com.seancoyle.core.domain.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideLaunchRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(OkHttpClient())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }
            readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
        }

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











