package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.spacex.framework.datasource.network.model.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val LAUNCH_OPTIONS_ROCKET = "rocket"
const val LAUNCH_OPTIONS_SORT = "desc"

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