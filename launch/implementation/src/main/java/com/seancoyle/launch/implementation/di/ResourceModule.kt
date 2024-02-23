package com.seancoyle.launch.implementation.di

import android.content.Context
import com.seancoyle.core.domain.StringResource
import com.seancoyle.core.presentation.StringResourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object ResourceModule {

    @Provides
    fun bindsStringResource(
        @ApplicationContext context: Context
    ): StringResource {
        return StringResourceImpl(
            context = context
        )
    }
}