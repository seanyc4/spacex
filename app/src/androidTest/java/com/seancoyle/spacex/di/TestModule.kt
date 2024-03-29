package com.seancoyle.spacex.di

import android.content.Context
import com.seancoyle.core_testing.JsonFileReader
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class]
)
object TestModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): HiltTestApplication {
        return app as HiltTestApplication
    }

    @Singleton
    @Provides
    fun providesJsonFileReader(application: HiltTestApplication): JsonFileReader {
        return JsonFileReader(
            application = application
        )
    }
}