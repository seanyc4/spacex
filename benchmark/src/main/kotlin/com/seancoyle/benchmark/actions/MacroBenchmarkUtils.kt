package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.ElementNotFoundException
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_TIMEOUT

internal fun UiAutomatorTestScope.findScrollableElement() = onElementOrNull { isScrollable }
    ?: throw ElementNotFoundException("Scrollable element not found")

private fun matchesTestTag(resourceName: String?, testTag: String): Boolean {
    if (resourceName == null) return false
    return resourceName == testTag || resourceName.endsWith(":id/$testTag")
}

internal fun UiAutomatorTestScope.findScrollableElement(testTag: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { isScrollable && matchesTestTag(viewIdResourceName, testTag) }
        ?: throw ElementNotFoundException("Scrollable element with id: $testTag not found")

internal fun UiAutomatorTestScope.findElementByText(textString: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { textAsString() == textString }
        ?: throw ElementNotFoundException("Element with text: $textString not found")

internal fun UiAutomatorTestScope.findElementByRes(resIdString: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { viewIdResourceName?.endsWith(":id/$resIdString") == true }
        ?: throw ElementNotFoundException("Could not find object with resource ID ending with :id/$resIdString")

internal fun UiAutomatorTestScope.findElementContainsRes(resIdString: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { viewIdResourceName?.contains(resIdString) == true }
        ?: throw ElementNotFoundException("Could not find object with resource ID:$resIdString")

internal fun UiAutomatorTestScope.findElementByTestTag(testTag: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { matchesTestTag(viewIdResourceName, testTag) }
        ?: throw ElementNotFoundException("Could not find object with testTag/resource ID: $testTag")
