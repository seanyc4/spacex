package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.rocket.RocketConfigurationCard
import com.seancoyle.feature.launch.presentation.launch.components.rocket.RocketSection
import com.seancoyle.feature.launch.presentation.launch.model.ConfigurationUI
import com.seancoyle.feature.launch.presentation.launch.model.RocketUI
import org.junit.Rule
import org.junit.Test

class RocketSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenRocketConfiguration_whenRocketSectionDisplayed_thenShowsRocketName() {
        val rocket = createTestRocket(name = "Falcon 9", fullName = "Falcon 9 Block 5")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("Falcon 9 Block 5")
            .assertIsDisplayed()
    }

    @Test
    fun givenRocketConfiguration_whenRocketSectionDisplayed_thenShowsFullName() {
        val rocket = createTestRocket(fullName = "Falcon 9 Block 5")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("Falcon 9 Block 5")
            .assertIsDisplayed()
    }

    @Test
    fun givenRocketConfiguration_whenRocketSectionDisplayed_thenShowsDescription() {
        val description = "A reusable two-stage rocket"
        val rocket = createTestRocket(description = description)

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText(description)
            .assertIsDisplayed()
    }

    @Test
    fun givenRocketWithStats_whenRocketSectionDisplayed_thenShowsTotalLaunchCount() {
        val rocket = createTestRocket(totalLaunchCount = "300")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("300")
            .assertIsDisplayed()
    }

    @Test
    fun givenRocketWithStats_whenRocketSectionDisplayed_thenShowsSuccessfulLaunches() {
        val rocket = createTestRocket(successfulLaunches = "290")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("290")
            .assertIsDisplayed()
    }

    @Test
    fun givenRocketWithDimensions_whenRocketSectionDisplayed_thenShowsLength() {
        val rocket = createTestRocket(length = "70.0 m")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("70.0 m")
            .assertExists()
    }

    @Test
    fun givenRocketWithDimensions_whenRocketSectionDisplayed_thenShowsDiameter() {
        val rocket = createTestRocket(diameter = "3.7 m")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("3.7 m")
            .assertExists()
    }

    @Test
    fun givenRocketWithVariant_whenRocketSectionDisplayed_thenShowsVariant() {
        val rocket = createTestRocket(variant = "Block 5")

        composeRule.setContent {
            AppTheme {
                RocketSection(rocket = rocket)
            }
        }

        composeRule.onNodeWithText("Block 5")
            .assertIsDisplayed()
    }

    @Test
    fun givenRocketConfigurationCard_whenDisplayed_thenShowsRocketConfigurationTitle() {
        val config = createTestConfiguration()

        composeRule.setContent {
            AppTheme {
                RocketConfigurationCard(config = config)
            }
        }

        composeRule.onNodeWithText("Rocket Configuration")
            .assertIsDisplayed()
    }

    private fun createTestRocket(
        name: String = "Falcon 9",
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        alias: String = "F9",
        description: String = "Falcon 9 rocket",
        totalLaunchCount: String = "300",
        successfulLaunches: String = "290",
        failedLaunches: String = "10",
        length: String = "70.0 m",
        diameter: String = "3.7 m",
        launchMass: String = "549,054 kg"
    ): RocketUI = RocketUI(
        configuration = createTestConfiguration(
            name = name,
            fullName = fullName,
            variant = variant,
            alias = alias,
            description = description,
            totalLaunchCount = totalLaunchCount,
            successfulLaunches = successfulLaunches,
            failedLaunches = failedLaunches,
            length = length,
            diameter = diameter,
            launchMass = launchMass
        ),
        launcherStages = emptyList(),
        spacecraftStages = emptyList()
    )

    private fun createTestConfiguration(
        name: String = "Falcon 9",
        fullName: String = "Falcon 9 Block 5",
        variant: String = "Block 5",
        alias: String = "F9",
        description: String = "Falcon 9 rocket",
        totalLaunchCount: String = "300",
        successfulLaunches: String = "290",
        failedLaunches: String = "10",
        length: String = "70.0 m",
        diameter: String = "3.7 m",
        launchMass: String = "549,054 kg"
    ): ConfigurationUI = ConfigurationUI(
        name = name,
        fullName = fullName,
        variant = variant,
        alias = alias,
        description = description,
        imageUrl = "https://example.com/rocket.jpg",
        totalLaunchCount = totalLaunchCount,
        successfulLaunches = successfulLaunches,
        failedLaunches = failedLaunches,
        length = length,
        diameter = diameter,
        launchMass = launchMass,
        maidenFlight = "2007-05-13",
        manufacturer = null,
        families = emptyList(),
        wikiUrl = null
    )
}
