package com.seancoyle.core.ui.adaptive

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class WindowSizeClassUtilTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun compactWidth_returnsSingleColumnConfig() {
        // Compact width phone (e.g., 360dp wide)
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))
        val config = AdaptiveLayoutConfig.fromWindowSizeClass(windowSizeClass)

        assertEquals(LayoutMode.SINGLE_COLUMN, config.layoutMode)
        assertEquals(1, config.listColumnCount)
        assertFalse(config.showDetailPane)
        assertTrue(config.useNavigationForDetail)
    }

    @Test
    fun mediumWidth_returnsTwoColumnConfig() {
        // Medium width (e.g., 700dp wide, landscape phone)
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(700.dp, 400.dp))
        val config = AdaptiveLayoutConfig.fromWindowSizeClass(windowSizeClass)

        assertEquals(LayoutMode.TWO_COLUMN, config.layoutMode)
        assertEquals(2, config.listColumnCount)
        assertFalse(config.showDetailPane)
        assertTrue(config.useNavigationForDetail)
    }

    @Test
    fun expandedWidth_returnsTwoPaneConfig() {
        // Expanded width (e.g., 900dp wide, tablet)
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 600.dp))
        val config = AdaptiveLayoutConfig.fromWindowSizeClass(windowSizeClass)

        assertEquals(LayoutMode.TWO_PANE, config.layoutMode)
        assertEquals(2, config.listColumnCount)
        assertTrue(config.showDetailPane)
        assertFalse(config.useNavigationForDetail)
    }

    @Test
    fun isExpandedWidth_returnsTrueForExpandedWidth() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 600.dp))
        assertTrue(windowSizeClass.isExpandedWidth)
    }

    @Test
    fun isExpandedWidth_returnsFalseForCompactWidth() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))
        assertFalse(windowSizeClass.isExpandedWidth)
    }

    @Test
    fun isExpandedWidth_returnsFalseForMediumWidth() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(700.dp, 400.dp))
        assertFalse(windowSizeClass.isExpandedWidth)
    }

    @Test
    fun shouldShowTwoColumns_returnsTrueForMediumWidth() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(700.dp, 400.dp))
        assertTrue(windowSizeClass.shouldShowTwoColumns)
    }

    @Test
    fun shouldShowTwoColumns_returnsTrueForExpandedWidth() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 600.dp))
        assertTrue(windowSizeClass.shouldShowTwoColumns)
    }

    @Test
    fun shouldShowTwoColumns_returnsFalseForCompactWidth() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))
        assertFalse(windowSizeClass.shouldShowTwoColumns)
    }

    @Test
    fun rememberAdaptiveLayoutConfig_returnsCorrectConfigForCompact() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 640.dp))

        composeRule.setContent {
            val config = rememberAdaptiveLayoutConfig(windowSizeClass)
            assertEquals(LayoutMode.SINGLE_COLUMN, config.layoutMode)
        }
    }

    @Test
    fun rememberAdaptiveLayoutConfig_returnsCorrectConfigForExpanded() {
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 600.dp))

        composeRule.setContent {
            val config = rememberAdaptiveLayoutConfig(windowSizeClass)
            assertEquals(LayoutMode.TWO_PANE, config.layoutMode)
        }
    }
}
