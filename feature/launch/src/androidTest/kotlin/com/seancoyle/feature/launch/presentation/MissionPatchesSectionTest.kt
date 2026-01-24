package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.MissionPatchesSection
import com.seancoyle.feature.launch.presentation.launch.model.MissionPatchUI
import org.junit.Rule
import org.junit.Test

class MissionPatchesSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenMissionPatches_whenSectionDisplayed_thenShowsSectionTitle() {
        val patches = listOf(createTestPatch())

        composeRule.setContent {
            AppTheme {
                MissionPatchesSection(patches = patches)
            }
        }

        composeRule.onNodeWithText("Mission Patches")
            .assertIsDisplayed()
    }

    @Test
    fun givenSingleMissionPatch_whenSectionDisplayed_thenShowsPatchName() {
        val patchName = "CRS-25 Patch"
        val patches = listOf(createTestPatch(name = patchName))

        composeRule.setContent {
            AppTheme {
                MissionPatchesSection(patches = patches)
            }
        }

        composeRule.onNodeWithText(patchName)
            .assertIsDisplayed()
    }

    @Test
    fun givenMultipleMissionPatches_whenSectionDisplayed_thenShowsAllPatchNames() {
        val patches = listOf(
            createTestPatch(name = "Patch Alpha"),
            createTestPatch(name = "Patch Beta")
        )

        composeRule.setContent {
            AppTheme {
                MissionPatchesSection(patches = patches)
            }
        }

        composeRule.onNodeWithText("Patch Alpha")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Patch Beta")
            .assertIsDisplayed()
    }

    @Test
    fun givenMissionPatch_whenSectionDisplayed_thenPatchImageHasContentDescription() {
        val patchName = "Dragon Patch"
        val patches = listOf(createTestPatch(name = patchName))

        composeRule.setContent {
            AppTheme {
                MissionPatchesSection(patches = patches)
            }
        }

        composeRule.onNodeWithContentDescription("Mission patch: $patchName")
            .assertExists()
    }

    private fun createTestPatch(
        name: String = "Test Patch",
        imageUrl: String = "https://example.com/patch.png",
        agencyName: String = "SpaceX"
    ): MissionPatchUI = MissionPatchUI(
        name = name,
        imageUrl = imageUrl,
        agencyName = agencyName
    )
}
