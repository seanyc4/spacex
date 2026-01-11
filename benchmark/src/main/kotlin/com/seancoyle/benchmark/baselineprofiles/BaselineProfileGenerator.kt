package com.seancoyle.benchmark.baselineprofiles

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.uiAutomator
import com.seancoyle.benchmark.BenchmarkConstants.ORBITAL
import com.seancoyle.benchmark.benchmarks.launch.launchJourney
import com.seancoyle.benchmark.benchmarks.launches.launchesJourney
import com.seancoyle.benchmark.benchmarks.launches.waitUntilLaunchesIsVisibleToUser
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Baseline Profile for app startup. This profile also enables using
 * [Dex Layout Optimizations](https://developer.android.com/topic/performance/baselineprofiles/dex-layout-optimizations)
 * via the `includeInStartupProfile` parameter.
 *
 * Run ./gradlew :app:generateBaselineProfile to generate using gradle managed device
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateStartupProfile() = baselineProfileRule.collect(
        packageName = ORBITAL,
        includeInStartupProfile = true
    ) {
        uiAutomator {
            startApp(ORBITAL)
            waitUntilLaunchesIsVisibleToUser()
        }
    }

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collect(
        packageName = ORBITAL,
    ) {
        uiAutomator {
            startApp(ORBITAL)
            launchesJourney()
            launchJourney()
        }
    }
}
