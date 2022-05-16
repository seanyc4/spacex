package com.seancoyle.spacex.di

import android.content.Context
import android.content.SharedPreferences
import com.seancoyle.spacex.framework.datasource.cache.implementation.datetransformer.DateTransformerImpl
import com.seancoyle.spacex.framework.datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.spacex.framework.datasource.network.implementation.dateformatter.DateFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.implementation.numberformatter.NumberFormatterImpl
import com.seancoyle.spacex.framework.datasource.network.model.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): HiltTestApplication {
        return app as HiltTestApplication
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
    ): DateFormatterImpl {
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
    fun provideDateTransformer(): DateTransformerImpl {
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






















