package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.ElementNotFoundException
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.seancoyle.benchmark.BenchmarkConstants.ORBITAL

internal fun UiAutomatorTestScope.findScrollableElement() = onElement { isScrollable }

internal fun UiAutomatorTestScope.findScrollableElement(resIdString: String) =
    onElementOrNull { isScrollable && viewIdResourceName == "$ORBITAL:id/$resIdString" }
        ?: throw ElementNotFoundException("Scrollable element with id: \"$ORBITAL:id/$resIdString\" not found")

internal fun UiAutomatorTestScope.findElementByText(textString: String) =
    onElementOrNull { textAsString() == textString }
        ?: throw ElementNotFoundException("Element with text: $textString not found")

internal fun UiAutomatorTestScope.findElementByRes(resIdString: String) =
    onElementOrNull { viewIdResourceName == "$ORBITAL:id/$resIdString" }
        ?: throw ElementNotFoundException("Could not find object with resource ID: $ORBITAL:id/$resIdString")

internal fun UiAutomatorTestScope.findElementContainsRes(resIdString: String) =
    onElementOrNull { viewIdResourceName?.contains(resIdString) == true }
        ?: throw ElementNotFoundException("Could not find object with resource ID:$resIdString")

internal fun UiAutomatorTestScope.findElementByTestTag(testTag: String) =
    onElementOrNull { (viewIdResourceName == testTag) }
        ?: throw ElementNotFoundException("Could not find object with resource ID: $testTag")
