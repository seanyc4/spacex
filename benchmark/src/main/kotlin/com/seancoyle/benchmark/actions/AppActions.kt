package com.seancoyle.benchmark.actions

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.UiDevice
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_TIMEOUT
import com.seancoyle.benchmark.BenchmarkConstants.ORBITAL

private const val INDEX = 1

internal fun UiAutomatorTestScope.scrollVerticalView(
    repeatDown: Int = 3,
    repeatUp: Int = 3,
    id: String? = null
) {
    waitForStableInActiveWindow()
    val element = id?.let { findScrollableElement(it) } ?: findScrollableElement()
    repeat(repeatDown) { element.fling(Direction.DOWN) }
    repeat(repeatUp) { element.fling(Direction.UP) }
}

internal fun UiAutomatorTestScope.scrollHorizontalView(
    repeat: Int = 3,
    id: String = ""
) {
    waitForStableInActiveWindow()
    repeat(repeat) {
        val element = findScrollableElement(id)
        element.fling(Direction.RIGHT)
        element.fling(Direction.LEFT)
    }
}

internal fun UiAutomatorTestScope.clickChildInView(id: String, index: Int = INDEX) {
    val view = findElementByTestTag(id)
    view.children.getOrNull(index)?.click() // default index 1 to avoid clicking on headers
    device.waitForIdle(DEFAULT_TIMEOUT)
}

internal fun deleteAppData() {
    val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    uiDevice.executeShellCommand("pm clear $ORBITAL")
}
