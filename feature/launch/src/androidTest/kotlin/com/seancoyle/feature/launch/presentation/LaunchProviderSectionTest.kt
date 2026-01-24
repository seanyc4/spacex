package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.LaunchProviderSection
import com.seancoyle.feature.launch.presentation.launch.model.AgencyUI
import org.junit.Rule
import org.junit.Test

class LaunchProviderSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenLaunchProvider_whenSectionDisplayed_thenShowsSectionTitle() {
        val agency = createTestAgency()

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText("Launch Provider")
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchProvider_whenSectionDisplayed_thenShowsAgencyName() {
        val agencyName = "SpaceX"
        val agency = createTestAgency(name = agencyName)

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText(agencyName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchProvider_whenSectionDisplayed_thenShowsAbbreviation() {
        val abbreviation = "SpX"
        val agency = createTestAgency(abbrev = abbreviation)

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText(abbreviation)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchProvider_whenSectionDisplayed_thenShowsType() {
        val agencyType = "Commercial"
        val agency = createTestAgency(type = agencyType)

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText(agencyType)
            .assertIsDisplayed()
    }

    @Test
    fun givenGovernmentAgency_whenSectionDisplayed_thenShowsCorrectType() {
        val agency = createTestAgency(
            name = "National Aeronautics and Space Administration",
            abbrev = "NASA",
            type = "Government"
        )

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText("NASA")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Government")
            .assertIsDisplayed()
    }

    private fun createTestAgency(
        name: String = "SpaceX",
        abbrev: String = "SpX",
        type: String = "Commercial",
        description: String = "Space Exploration Technologies Corp."
    ): AgencyUI = AgencyUI(
        name = name,
        abbrev = abbrev,
        type = type,
        description = description
    )
}
