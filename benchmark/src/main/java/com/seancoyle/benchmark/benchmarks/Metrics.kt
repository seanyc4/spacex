@file:OptIn(ExperimentalMetricApi::class)

package com.seancoyle.benchmark.benchmarks

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.TraceSectionMetric

/**
 * A [TraceSectionMetric] that tracks the time spent configuring Just-in-time compilation classes.
 */
internal val jitCompilationMetric = TraceSectionMetric(
    sectionName = "JIT Compiling %", label = "JIT Compilation"
)

/**
 *  A [TraceSectionMetric] that tracks the time spent in class initialisation.
 *  This number should go down when a baseline profile is applied
 */
internal val classInitMetric = TraceSectionMetric(
    sectionName = "L%/%;", label = "ClassInit"
)
