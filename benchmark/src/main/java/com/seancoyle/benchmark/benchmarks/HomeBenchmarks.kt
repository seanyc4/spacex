package com.seancoyle.benchmark.benchmarks

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
import com.seancoyle.benchmark.BenchmarkConstants.SPACEX
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_ITERATIONS
import com.seancoyle.benchmark.actions.scrollHorizontalView
import com.seancoyle.benchmark.actions.scrollVerticalView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun verticalScrollCompilationNone() =
        verticalScrollBenchmark(CompilationMode.None())

    @Test
    fun verticalScrollCompilationBaselineProfiles() =
        verticalScrollBenchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    @Test
    fun horizontalScrollCompilationNone() =
        horizontalScrollBenchmark(CompilationMode.None())

    @Test
    fun horizontalScrollCompilationBaselineProfiles() =
        horizontalScrollBenchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun verticalScrollBenchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = SPACEX,
            metrics = listOf(
                StartupTimingMetric(),
                FrameTimingMetric(),
                jitCompilationMetric,
                classInitMetric
            ),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = DEFAULT_ITERATIONS,
            setupBlock = { killProcess() },
            measureBlock = {
                uiAutomator {
                    startApp(SPACEX)
                    scrollVerticalView()
                }
            }
        )
    }

    private fun horizontalScrollBenchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = SPACEX,
            metrics = listOf(
                StartupTimingMetric(),
                FrameTimingMetric(),
                jitCompilationMetric,
                classInitMetric
            ),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = DEFAULT_ITERATIONS,
            setupBlock = { killProcess() },
            measureBlock = {
                uiAutomator {
                    startApp(SPACEX)
                    scrollHorizontalView(id = "LaunchCarouselRow")
                }
            }
        )
    }
}
