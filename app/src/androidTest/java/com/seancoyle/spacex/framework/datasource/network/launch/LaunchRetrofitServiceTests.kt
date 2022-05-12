package com.seancoyle.spacex.framework.datasource.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.di.AppModule
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.di.ProductionModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith

/*
    LEGEND:
    1. CBS = "Confirm by searching"

    Test cases:
    1. insert a single item, CBS


 */
@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    LaunchModule::class,
    AppModule::class,
    ProductionModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchRetrofitServiceTests: BaseTest(){


}





































