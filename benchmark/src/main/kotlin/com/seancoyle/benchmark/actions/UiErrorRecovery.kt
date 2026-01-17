package com.seancoyle.benchmark.actions

import androidx.test.uiautomator.UiAutomatorTestScope
import androidx.test.uiautomator.textAsString
import com.seancoyle.benchmark.BenchmarkConstants.DEFAULT_TIMEOUT
import com.seancoyle.core.test.testags.LaunchesTestTags

internal fun UiAutomatorTestScope.clickRetryIfVisible(
    timeoutMs: Long = 250,
    retryButtonTestTag: String = LaunchesTestTags.RETRY_BUTTON,
    fallbackTexts: List<String> = listOf("Retry"),
): Boolean {
    // Prefer stable testTag first
    val retryByTag = onElementOrNull(timeoutMs) {
        isVisibleToUser && viewIdResourceName?.let { it == retryButtonTestTag || it.endsWith(":id/$retryButtonTestTag") } == true
    }

    val retryObject = retryByTag ?: onElementOrNull(timeoutMs) {
        isVisibleToUser && fallbackTexts.any { fallback -> textAsString().equals(fallback, ignoreCase = true) }
    }

    if (retryObject != null) {
        retryObject.click()
        return true
    }

    return false
}

internal fun UiAutomatorTestScope.waitForOrRecoverFromError(
    timeoutMs: Long = DEFAULT_TIMEOUT,
    pollIntervalMs: Long = 250,
    errorStateTestTag: String = LaunchesTestTags.ERROR_STATE,
    loadingStateTestTag: String = LaunchesTestTags.LOADING_STATE,
    retryButtonTestTag: String = LaunchesTestTags.RETRY_BUTTON,
    successCondition: UiAutomatorTestScope.() -> Boolean,
): Boolean {
    val start = System.currentTimeMillis()
    var lastRetryClickAt = 0L

    while (System.currentTimeMillis() - start < timeoutMs) {
        if (successCondition()) return true

        // If we're in loading state, just keep polling.
        val loadingVisible = onElementOrNull(0) {
            isVisibleToUser && viewIdResourceName?.let { it == loadingStateTestTag || it.endsWith(":id/$loadingStateTestTag") } == true
        } != null
        if (loadingVisible) {
            Thread.sleep(pollIntervalMs)
            continue
        }

        // If error state is visible, attempt recovery.
        val errorVisible = onElementOrNull(0) {
            isVisibleToUser && viewIdResourceName?.let { it == errorStateTestTag || it.endsWith(":id/$errorStateTestTag") } == true
        } != null

        if (errorVisible) {
            // Avoid hammering retry too quickly.
            val now = System.currentTimeMillis()
            if (now - lastRetryClickAt > 750) {
                val clicked = clickRetryIfVisible(
                    timeoutMs = pollIntervalMs,
                    retryButtonTestTag = retryButtonTestTag,
                )
                if (clicked) lastRetryClickAt = now
            }
        }

        Thread.sleep(pollIntervalMs)
    }

    return successCondition()
}
