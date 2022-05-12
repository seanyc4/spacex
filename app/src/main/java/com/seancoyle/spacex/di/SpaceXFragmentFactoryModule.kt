package com.seancoyle.spacex.di

import androidx.fragment.app.FragmentFactory
import com.seancoyle.spacex.framework.presentation.common.SpaceXFragmentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object SpaceXFragmentFactoryModule {

    @Singleton
    @Provides
    fun provideSpaceXFragmentFactory(): FragmentFactory {
        return SpaceXFragmentFactory()
    }

}