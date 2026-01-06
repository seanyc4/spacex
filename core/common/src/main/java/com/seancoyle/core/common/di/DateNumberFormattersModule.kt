package com.seancoyle.core.common.di

import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.dataformatter.DateTransformerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DateNumberFormattersModule {

    @Binds
    abstract fun provideDateTransformer(
        impl: DateTransformerImpl
    ): DateTransformer
}
