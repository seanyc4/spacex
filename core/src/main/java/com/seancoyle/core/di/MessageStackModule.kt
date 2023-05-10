package com.seancoyle.core.di

import com.seancoyle.core.domain.MessageStack
import com.seancoyle.core.domain.MessageStackImpl
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