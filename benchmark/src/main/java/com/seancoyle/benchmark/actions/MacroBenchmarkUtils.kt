package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.ElementNotFoundException
import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.seancoyle.benchmark.BenchmarkConstants.SPACEX

internal fun UiAutomatorTestScope.findScrollableElement(resIdString: String) =
    if (resIdString.isNotEmpty()) {
        onElementOrNull { isScrollable && viewIdResourceName == "$SPACEX:id/$resIdString" }
    } else {
        onElementOrNull { isScrollable }
    } ?: throw ElementNotFoundException("Scrollable element with id: \"$SPACEX:id/$resIdString\" not found")

internal fun UiAutomatorTestScope.findTextAsString(textString: String) =
    onElementOrNull { textAsString() == textString }
        ?: throw ElementNotFoundException("Element with text: $textString not found")

internal fun UiAutomatorTestScope.findObjectByRes(resIdString: String) =
    onElementOrNull { viewIdResourceName == "$SPACEX:id/$resIdString" }
        ?: throw ElementNotFoundException("Could not find object with resource ID: $SPACEX:id/$resIdString")