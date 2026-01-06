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
import androidx.test.uiautomator.textAsString
import androidx.test.uiautomator.uiAutomator
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_ITERATIONS
import com.seancoyle.benchmark.BenchmarkConstants.ORBITAL
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
            packageName = ORBITAL,
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
                    startApp(ORBITAL)
                    onElement { textAsString() == "HEADER" && isVisibleToUser }
                }
            }
        )
    }
}
