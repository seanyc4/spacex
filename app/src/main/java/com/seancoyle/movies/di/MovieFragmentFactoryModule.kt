package com.seancoyle.movies.di

import androidx.fragment.app.FragmentFactory
import com.seancoyle.movies.framework.presentation.common.MovieFragmentFactory
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
object MovieFragmentFactoryModule {

    @Singleton
    @Provides
    fun provideMovieFragmentFactory(): FragmentFactory {
        return MovieFragmentFactory()
    }

}