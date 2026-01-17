package com.seancoyle.benchmark.actions

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import timber.log.Timber
import java.io.File

private const val TAG = "UiDiagnostics"

/**
 * Writes debug artifacts that are easy to inspect on CI when running on Gradle Managed Devices.
 *
 * Artifacts are written into the instrumentation arg `additionalTestOutputDir` when present,
 * which AGP already copies back to the host after the test run.
 */
internal object UiDiagnostics {

    fun dumpWindowHierarchyToAdditionalOutput(fileName: String) {
        runCatching {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val outDir = additionalTestOutputDir() ?: run {
                Timber.tag(TAG).w("additionalTestOutputDir not set; skipping hierarchy dump")
                return
            }
            outDir.mkdirs()
            val outFile = File(outDir, fileName)
            val ok = device.dumpWindowHierarchy(outFile)
            Timber.tag(TAG).i("dumpWindowHierarchy -> ${outFile.absolutePath}, ok=$ok")
        }.onFailure { t ->
            Timber.tag(TAG).e(t, "dumpWindowHierarchy failed")
        }
    }

    fun takeScreenshotToAdditionalOutput(fileName: String) {
        // Some GMD system images (notably aosp-atd) can return 0x0 screenshots.
        // We still try, but never let this crash the test.
        runCatching {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val outDir = additionalTestOutputDir() ?: run {
                Timber.tag(TAG).w("additionalTestOutputDir not set; skipping screenshot")
                return
            }
            outDir.mkdirs()
            val outFile = File(outDir, fileName)
            val ok = device.takeScreenshot(outFile)
            Timber.tag(TAG).i("takeScreenshot -> ${outFile.absolutePath}, ok=$ok")
        }.onFailure { t ->
            Timber.tag(TAG).e(t, "takeScreenshot failed")
        }
    }

    private fun additionalTestOutputDir(): File? {
        val args = InstrumentationRegistry.getArguments()
        val path = args.getString("additionalTestOutputDir")
        if (path.isNullOrBlank()) return null
        // The arg is typically something like /sdcard/Android/media/<testPkg>/additional_test_output
        return File(path)
    }
}
