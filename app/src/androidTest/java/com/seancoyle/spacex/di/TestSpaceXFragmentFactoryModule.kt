package com.seancoyle.spacex.di

import androidx.fragment.app.FragmentFactory
import com.seancoyle.spacex.framework.presentation.TestSpaceXFragmentFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SpaceXFragmentFactoryModule::class]
)
object TestSpaceXFragmentFactoryModule {

    @Singleton
    @Provides
    fun provideSpaceXFragmentFactory(
    ): FragmentFactory {
        return TestSpaceXFragmentFactory()
    }
}