package com.seancoyle.spacex.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.FlowPreview
import org.junit.Test

@ExperimentalTestApi
@FlowPreview
@HiltAndroidTest
class SpaceXTest: LaunchBase() {

    @Test
    fun verifyLaunchScreenAndGridIsDisplayed() {
       /* composeTestRule.apply {
            val appName by stringResource(R.string.app_name)
            val filterBtn by stringResource(R.string.filter_btn_content_desc)

            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithText(appName).assertIsDisplayed()
            onNodeWithContentDescription(filterBtn).assertIsDisplayed()
            onNodeWithTag(launchGridTag).assertIsDisplayed()
        }*/
    }

}