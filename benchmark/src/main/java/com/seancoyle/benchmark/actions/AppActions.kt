package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.ElementNotFoundException
import androidx.test.uiautomator.UiAutomatorTestScope

internal fun UiAutomatorTestScope.scrollVerticalView(
    repeatDown: Int = 3,
    repeatUp: Int = 3,
    id: String = ""
) {
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
    repeat(repeat) {
        val element = findScrollableElement(id)
        element.fling(Direction.RIGHT)
        element.fling(Direction.LEFT)
    }
}

private fun UiAutomatorTestScope.findScrollableElement(id: String) =
    if (id.isNotEmpty()) {
        onElementOrNull { isScrollable && contentDescription == id }
    } else {
        onElementOrNull { isScrollable }
    } ?: throw ElementNotFoundException("Scrollable element with id: $id not found")
