package com.seancoyle.benchmark.benchmarks.launch

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.uiAutomator
import com.seancoyle.benchmark.BenchmarkConstants
import com.seancoyle.benchmark.actions.clickChildInView
import com.seancoyle.benchmark.actions.deleteAppData
import com.seancoyle.benchmark.actions.findElementByTestTag
import com.seancoyle.benchmark.actions.scrollVerticalView
import com.seancoyle.benchmark.benchmarks.classInitMetric
import com.seancoyle.benchmark.benchmarks.jitCompilationMetric
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCH_CARD
import com.seancoyle.core.test.testags.LaunchesTestTags.PAST_TAB
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
class LaunchBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollCompilationNone() =
        launchScrollBenchmark(CompilationMode.None())

    @Test
    fun scrollCompilationBaselineProfiles() =
        launchScrollBenchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun launchScrollBenchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = BenchmarkConstants.ORBITAL,
            metrics = listOf(
                StartupTimingMetric(),
                FrameTimingMetric(),
                jitCompilationMetric,
                classInitMetric
            ),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = BenchmarkConstants.DEFAULT_ITERATIONS,
            setupBlock = {
                uiAutomator {
                    deleteAppData()
                    startApp(BenchmarkConstants.ORBITAL)
                    findElementByTestTag(PAST_TAB).click()
                    clickChildInView(LAUNCH_CARD, 2)
                }
            },
            measureBlock = {
                uiAutomator {
                    scrollVerticalView()
                }
            }
        )
    }
}