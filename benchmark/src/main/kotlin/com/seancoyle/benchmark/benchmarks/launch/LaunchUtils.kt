package com.seancoyle.benchmark.benchmarks.launch

import androidx.test.uiautomator.UiAutomatorTestScope
import com.seancoyle.benchmark.actions.clickChildInView
import com.seancoyle.benchmark.actions.scrollVerticalView
import com.seancoyle.core.test.testags.LaunchesTestTags.LAUNCH_CARD

internal fun UiAutomatorTestScope.launchJourney() {
    clickChildInView(LAUNCH_CARD, 3)
    scrollVerticalView(1, 1)
}
