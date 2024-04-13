package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.LaunchConstants.LAUNCH_OPTIONS_ROCKET
import com.seancoyle.launch.api.LaunchConstants.LAUNCH_OPTIONS_SORT
import com.seancoyle.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.model.Options
import com.seancoyle.launch.implementation.domain.model.Populate
import com.seancoyle.launch.implementation.domain.model.Select
import com.seancoyle.launch.implementation.domain.model.Sort
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object LaunchOptionsModule {

    @Provides
    fun provideLaunchOptions(): LaunchOptions {
        return LaunchOptions(
            options = Options(
                populate = listOf(
                    Populate(
                        path = LAUNCH_OPTIONS_ROCKET,
                        select = Select(
                            name = 1,
                            type = 2
                        )
                    )
                ),
                sort = Sort(
                    flightNumber = LAUNCH_OPTIONS_SORT,
                ),
                limit = 500
            )
        )
    }
}