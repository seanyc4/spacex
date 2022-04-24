package com.seancoyle.movies.di

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.framework.presentation.common.MovieFragmentFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
object FragmentFactoryModule {

    @Singleton
    @Provides
    fun provideFragmentFactory(
        viewModelFactory: ViewModelProvider.Factory
    ): FragmentFactory {
        return MovieFragmentFactory(
            viewModelFactory
        )
    }
}