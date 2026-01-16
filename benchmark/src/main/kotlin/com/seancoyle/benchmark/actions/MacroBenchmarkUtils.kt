package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.ElementNotFoundException
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_TIMEOUT
import com.seancoyle.benchmark.BenchmarkConstants.ORBITAL

internal fun UiAutomatorTestScope.findScrollableElement() = onElement { isScrollable }

internal fun UiAutomatorTestScope.findScrollableElement(testTag: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { isScrollable && viewIdResourceName == testTag }
        ?: throw ElementNotFoundException("Scrollable element with id: $testTag not found")

internal fun UiAutomatorTestScope.findElementByText(textString: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { textAsString() == textString }
        ?: throw ElementNotFoundException("Element with text: $textString not found")

internal fun UiAutomatorTestScope.findElementByRes(resIdString: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { viewIdResourceName == "$ORBITAL:id/$resIdString" }
        ?: throw ElementNotFoundException("Could not find object with resource ID: $ORBITAL:id/$resIdString")

internal fun UiAutomatorTestScope.findElementContainsRes(resIdString: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { viewIdResourceName?.contains(resIdString) == true }
        ?: throw ElementNotFoundException("Could not find object with resource ID:$resIdString")

internal fun UiAutomatorTestScope.findElementByTestTag(testTag: String) =
    onElementOrNull(DEFAULT_TIMEOUT) { viewIdResourceName == testTag }
        ?: throw ElementNotFoundException("Could not find object with resource ID: $testTag")
