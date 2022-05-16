package com.seancoyle.spacex.di

import android.content.Context
import android.content.SharedPreferences
import com.seancoyle.spacex.BuildConfig
import com.seancoyle.spacex.business.data.network.NetworkConstants.NETWORK_TIMEOUT
import com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.datasource.cache.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.spacex.framework.datasource.network.abstraction.dateformatter.DateFormatter
import com.seancoyle.spacex.framework.datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.spacex.framework.datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.implementation.numberformatter.NumberFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.model.launch.*
import com.seancoyle.spacex.framework.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
const val LAUNCH_OPTIONS_ROCKET = "rocket"
const val LAUNCH_OPTIONS_SORT = "desc"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

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
    fun provideSharedPrefsEditor(
        sharedPreferences: SharedPreferences
    ): SharedPreferences.Editor {
        return sharedPreferences.edit()
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


    @Singleton
    @Provides
    fun provideLaunchOptions(): LaunchOptions {
        return LaunchOptions(
            options = Options(
                populate = listOf(
                    Populate(
                        path = LAUNCH_OPTIONS_ROCKET,
                        select = Select(
                            name = 1,
                            type =2
                        )
                    )
                ),
                sort = Sort(
                    flight_number = LAUNCH_OPTIONS_SORT,
                ),
                limit =500
            )
        )
    }

}











