package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_OPTIONS_ROCKET
import com.seancoyle.constants.LaunchNetworkConstants.LAUNCH_OPTIONS_SORT
import com.seancoyle.launch_models.model.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchOptionsModule {

    @Singleton
    @Provides
    fun provideLaunchOptions(): LaunchOptions {
        return LaunchOptions(
            options = Options(
                populate = listOf(
                    Populate(
                        path = LAUNCH_OPTIONS_ROCKET,
                        select = Select(
                            name = 1,
                            type =2
                        )
                    )
                ),
                sort = Sort(
                    flight_number = LAUNCH_OPTIONS_SORT,
                ),
                limit =500
            )
        )
    }
}