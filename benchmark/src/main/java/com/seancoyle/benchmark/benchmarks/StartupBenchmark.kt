package com.seancoyle.benchmark.benchmarks

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.seancoyle.benchmark.BenchmarkConstants.APP_PACKAGE_NAME
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_ITERATIONS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
            packageName = APP_PACKAGE_NAME,
            metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = DEFAULT_ITERATIONS,
            setupBlock = {
                pressHome()
            },
            measureBlock = {
                startActivityAndWait()
            }
        )
    }
}