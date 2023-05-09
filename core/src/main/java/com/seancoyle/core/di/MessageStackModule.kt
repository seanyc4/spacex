package com.seancoyle.core.di

import com.seancoyle.core.state.MessageStack
import com.seancoyle.core.state.MessageStackImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MessageStackModule {

    @Binds
    abstract fun bindsMessageStack(
        impl: MessageStackImpl
    ): MessageStack
}