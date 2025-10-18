package com.seancoyle.benchmark.baselineprofiles

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.seancoyle.benchmark.BenchmarkConstants.APP_PACKAGE_NAME
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Baseline Profile for app startup. This profile also enables using [Dex Layout Optimizations](https://developer.android.com/topic/performance/baselineprofiles/dex-layout-optimizations)
 * via the `includeInStartupProfile` parameter.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBaselineProfile {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        APP_PACKAGE_NAME,
        includeInStartupProfile = true,
        profileBlock = MacrobenchmarkScope::startActivityAndWait,
    )

}
