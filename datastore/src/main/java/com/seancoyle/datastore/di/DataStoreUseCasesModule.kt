package com.seancoyle.datastore.di

import com.seancoyle.datastore.domain.ClearDataUseCase
import com.seancoyle.datastore.domain.ClearDataUseCaseImpl
import com.seancoyle.datastore.domain.ReadIntUseCase
import com.seancoyle.datastore.domain.ReadIntUseCaseImpl
import com.seancoyle.datastore.domain.ReadStringUseCase
import com.seancoyle.datastore.domain.ReadStringUseCaseImpl
import com.seancoyle.datastore.domain.SaveIntUseCase
import com.seancoyle.datastore.domain.SaveIntUseCaseImpl
import com.seancoyle.datastore.domain.SaveStringUseCase
import com.seancoyle.datastore.domain.SaveStringUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class DataStoreUseCasesModule {

    @Binds
    abstract fun bindsClearDataUseCase(
        impl: ClearDataUseCaseImpl
    ): ClearDataUseCase

    @Binds
    abstract fun bindsReadIntUseCase(
        impl: ReadIntUseCaseImpl
    ): ReadIntUseCase

    @Binds
    abstract fun bindsReadStringUseCase(
        impl: ReadStringUseCaseImpl
    ): ReadStringUseCase

    @Binds
    abstract fun bindsSaveStringUseCase(
        impl: SaveStringUseCaseImpl
    ): SaveStringUseCase

    @Binds
    abstract fun bindsSaveIntUseCase(
        impl: SaveIntUseCaseImpl
    ): SaveIntUseCase
}