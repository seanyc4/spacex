package com.seancoyle.benchmark.benchmarks.startup

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
import com.seancoyle.benchmark.actions.deleteAppData
import com.seancoyle.benchmark.benchmarks.classInitMetric
import com.seancoyle.benchmark.benchmarks.jitCompilationMetric
import com.seancoyle.benchmark.benchmarks.launches.waitUntilLaunchesIsVisibleToUser
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMetricApi::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmark {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        startupBenchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        startupBenchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun startupBenchmark(compilationMode: CompilationMode) {
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
                    waitUntilLaunchesIsVisibleToUser()
                }
            }
        )
    }
}
