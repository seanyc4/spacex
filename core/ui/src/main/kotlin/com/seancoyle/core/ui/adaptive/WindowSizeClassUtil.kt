package com.seancoyle.core.ui.adaptive

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

/**
 * Layout mode derived from WindowSizeClass.
 * Determines whether to use single-pane or two-pane layout,
 * and how many columns to display in grids.
 */
@Stable
enum class LayoutMode {
    /**
     * Single column layout with navigation between screens.
     * Used for: Portrait phones (WindowWidthSizeClass.Compact)
     */
    SINGLE_COLUMN,

    /**
     * Two column grid with navigation between screens.
     * Used for: Landscape phones (WindowWidthSizeClass.Medium)
     */
    TWO_COLUMN,

    /**
     * Two-pane layout with list and detail side by side.
     * Used for: Tablets/Foldables/Desktop (WindowWidthSizeClass.Expanded)
     */
    TWO_PANE
}

/**
 * Configuration for adaptive layouts based on WindowSizeClass.
 */
@Stable
data class AdaptiveLayoutConfig(
    val layoutMode: LayoutMode,
    val listColumnCount: Int,
    val showDetailPane: Boolean,
    val useNavigationForDetail: Boolean
) {
    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): AdaptiveLayoutConfig {
            return when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> AdaptiveLayoutConfig(
                    layoutMode = LayoutMode.SINGLE_COLUMN,
                    listColumnCount = 1,
                    showDetailPane = false,
                    useNavigationForDetail = true
                )
                WindowWidthSizeClass.Medium -> AdaptiveLayoutConfig(
                    layoutMode = LayoutMode.TWO_COLUMN,
                    listColumnCount = 2,
                    showDetailPane = false,
                    useNavigationForDetail = true
                )
                WindowWidthSizeClass.Expanded -> AdaptiveLayoutConfig(
                    layoutMode = LayoutMode.TWO_PANE,
                    listColumnCount = 2,
                    showDetailPane = true,
                    useNavigationForDetail = false
                )
                else -> AdaptiveLayoutConfig(
                    layoutMode = LayoutMode.SINGLE_COLUMN,
                    listColumnCount = 1,
                    showDetailPane = false,
                    useNavigationForDetail = true
                )
            }
        }
    }
}

/**
 * Remembers an AdaptiveLayoutConfig based on WindowSizeClass.
 * This is the primary entry point for composables to adapt their layout.
 */
@Composable
fun rememberAdaptiveLayoutConfig(windowSizeClass: WindowSizeClass): AdaptiveLayoutConfig {
    return remember(windowSizeClass) {
        AdaptiveLayoutConfig.fromWindowSizeClass(windowSizeClass)
    }
}

/**
 * Extension to check if the current layout should show a two-pane layout.
 */
val WindowSizeClass.isExpandedWidth: Boolean
    get() = widthSizeClass == WindowWidthSizeClass.Expanded

/**
 * Extension to check if the current layout should show two columns in lists/grids.
 */
val WindowSizeClass.shouldShowTwoColumns: Boolean
    get() = widthSizeClass == WindowWidthSizeClass.Medium ||
            widthSizeClass == WindowWidthSizeClass.Expanded

