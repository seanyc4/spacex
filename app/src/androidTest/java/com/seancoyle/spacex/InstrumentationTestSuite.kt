package com.seancoyle.spacex

import com.seancoyle.spacex.framework.datasource.cache.launch.LaunchDaoServiceTests
import com.seancoyle.spacex.framework.datasource.network.launch.LaunchRetrofitServiceTests
import com.seancoyle.spacex.framework.presentation.launch.LaunchFragmentEndToEndTest
import com.seancoyle.spacex.framework.presentation.launch.LaunchFragmentTests
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    LaunchDaoServiceTests::class,
    LaunchRetrofitServiceTests::class,
    LaunchFragmentTests::class,
    LaunchFragmentEndToEndTest::class
)
class InstrumentationTestSuite

























