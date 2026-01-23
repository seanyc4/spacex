package com.seancoyle.benchmark.benchmarks.launches

import androidx.test.uiautomator.UiAutomatorTestScope
import com.seancoyle.benchmark.actions.findElementByTestTag
import com.seancoyle.benchmark.actions.scrollVerticalView
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCHES_SCREEN
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCH_CARD
import com.seancoyle.core.test.testags.LaunchesTestTags.PAST_TAB

internal fun UiAutomatorTestScope.waitUntilLaunchesIsVisibleToUser() {
    onElementOrNull {
        viewIdResourceName?.contains(LAUNCH_CARD) == true && isVisibleToUser
    } ?: throw Exception("$LAUNCH_CARD is not visible to user")
}

internal fun UiAutomatorTestScope.launchesJourney() {
    scrollVerticalView(id = LAUNCHES_SCREEN)
    findElementByTestTag(PAST_TAB).click()
    scrollVerticalView(id = LAUNCHES_SCREEN)
}
