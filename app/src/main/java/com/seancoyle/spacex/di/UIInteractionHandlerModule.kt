package com.seancoyle.spacex.di

import android.app.Activity
import com.seancoyle.core.domain.UIInteractionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object UIInteractionHandlerModule {

    @Provides
    fun provideUIInteractionHandler(activity: Activity): UIInteractionHandler {
        return activity as? UIInteractionHandler
            ?: throw IllegalStateException("The Activity must implement UIInteractionHandler")
    }
}