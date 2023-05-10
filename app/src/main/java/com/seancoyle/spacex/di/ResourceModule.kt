package com.seancoyle.spacex.di

import android.content.Context
import com.seancoyle.core.util.StringResource
import com.seancoyle.spacex.presentation.StringResourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {

    @Provides
    fun bindsStringResource(
        @ApplicationContext context: Context
    ): StringResource {
        return StringResourceImpl(
            context = context
        )
    }
}