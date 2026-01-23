package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.LaunchSiteSection
import com.seancoyle.feature.launch.presentation.launch.model.PadUI
import org.junit.Rule
import org.junit.Test

class LaunchLocationSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenLaunchSiteSection_whenDisplayed_thenShowsLocationTitle() {
        val pad = createTestPad()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = pad)
            }
        }

        composeRule.onNodeWithText("Location")
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchSiteSection_whenDisplayed_thenShowsPadName() {
        val padName = "Space Launch Complex 40"
        val pad = createTestPad(name = padName)

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = pad)
            }
        }

        composeRule.onNodeWithText(padName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchSiteSection_whenDisplayed_thenShowsLocationName() {
        val locationName = "Cape Canaveral, FL, USA"
        val pad = createTestPad(locationName = locationName)

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = pad)
            }
        }

        composeRule.onNodeWithText(locationName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchSiteSection_whenDisplayed_thenShowsCountryName() {
        val countryName = "United States"
        val pad = createTestPad(countryName = countryName)

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = pad)
            }
        }

        composeRule.onNodeWithText(countryName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchSiteWithStats_whenDisplayed_thenShowsTotalLaunchCount() {
        val totalLaunchCount = "150"
        val pad = createTestPad(totalLaunchCount = totalLaunchCount)

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = pad)
            }
        }

        composeRule.onNodeWithText(totalLaunchCount)
            .assertIsDisplayed()
    }

    @Test
    fun givenInternationalLaunchSite_whenDisplayed_thenShowsCorrectCountry() {
        val pad = createTestPad(
            name = "Baikonur Cosmodrome",
            locationName = "Baikonur, Kazakhstan",
            countryName = "Kazakhstan",
            countryCode = "KAZ"
        )

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = pad)
            }
        }

        composeRule.onNodeWithText("Baikonur Cosmodrome")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Kazakhstan")
            .assertIsDisplayed()
    }

    private fun createTestPad(
        name: String = "Space Launch Complex 40",
        locationName: String = "Cape Canaveral, FL, USA",
        countryName: String = "United States",
        countryCode: String = "USA",
        imageUrl: String = "https://example.com/pad.jpg",
        description: String = "Launch pad at Cape Canaveral",
        latitude: String = "28.56194122",
        longitude: String = "-80.57735736",
        totalLaunchCount: String = "100",
        orbitalLaunchAttemptCount: String = "95",
        locationTotalLaunchCount: String = "1500",
        locationTotalLandingCount: String = "300",
        mapUrl: String = "https://example.com/map",
        mapImage: String = "https://example.com/map_image.png"
    ): PadUI = PadUI(
        name = name,
        locationName = locationName,
        countryName = countryName,
        countryCode = countryCode,
        imageUrl = imageUrl,
        description = description,
        latitude = latitude,
        longitude = longitude,
        totalLaunchCount = totalLaunchCount,
        orbitalLaunchAttemptCount = orbitalLaunchAttemptCount,
        locationTotalLaunchCount = locationTotalLaunchCount,
        locationTotalLandingCount = locationTotalLandingCount,
        mapUrl = mapUrl,
        mapImage = mapImage
    )
}
