package com.seancoyle.spacex.di

import android.content.Context
import com.seancoyle.spacex.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext context: Context): BaseApplication {
        return context as BaseApplication
    }
}












