package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiAutomatorTestScope
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_TIMEOUT

private const val INDEX = 1

internal fun UiAutomatorTestScope.scrollVerticalView(
    repeatDown: Int = 3,
    repeatUp: Int = 3,
    id: String = ""
) {
    waitForStableInActiveWindow()
    repeat(repeatDown) {
        findScrollableElement(id).fling(Direction.DOWN)
    }
    repeat(repeatUp) {
        findScrollableElement(id).fling(Direction.UP)
    }
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

internal fun UiAutomatorTestScope.clickFirstChildInView(id: String) {
    val view = findObjectByRes(id)
    device.waitForIdle(DEFAULT_TIMEOUT)
    val firstChild = view.children.getOrNull(INDEX) // index 1 to avoid clicking on headers
    firstChild?.click()
    device.waitForIdle(DEFAULT_TIMEOUT)
}

