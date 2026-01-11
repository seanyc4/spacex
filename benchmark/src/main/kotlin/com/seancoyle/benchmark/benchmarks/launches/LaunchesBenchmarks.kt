package com.seancoyle.benchmark.benchmarks.launches

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.uiAutomator
import com.seancoyle.benchmark.BenchmarkConstants
import com.seancoyle.benchmark.actions.deleteAppData
import com.seancoyle.benchmark.benchmarks.classInitMetric
import com.seancoyle.benchmark.benchmarks.jitCompilationMetric
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
class LaunchesBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollCompilationNone() =
        launchesScrollBenchmark(CompilationMode.None())

    @Test
    fun scrollCompilationBaselineProfiles() =
        launchesScrollBenchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun launchesScrollBenchmark(compilationMode: CompilationMode) {
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
            setupBlock = { deleteAppData() },
            measureBlock = {
                uiAutomator {
                    startApp(BenchmarkConstants.ORBITAL)
                    launchesJourney()
                }
            }
        )
    }
}
