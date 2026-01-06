package com.seancoyle.benchmark.baselineprofiles

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.uiAutomator
import com.seancoyle.benchmark.BenchmarkConstants.ORBITAL
import com.seancoyle.benchmark.actions.scrollHorizontalView
import com.seancoyle.benchmark.actions.scrollVerticalView
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCH_CAROUSEL_ROW
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
        packageName = ORBITAL,
        includeInStartupProfile = true
    ) {
        uiAutomator {
            startApp(ORBITAL)
            scrollHorizontalView(id = LAUNCH_CAROUSEL_ROW)
            scrollVerticalView()
        }
    }

}
