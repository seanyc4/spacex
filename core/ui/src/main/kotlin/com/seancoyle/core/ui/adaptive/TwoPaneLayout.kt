package com.seancoyle.core.ui.adaptive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Test tags for adaptive layout components.
 */
object AdaptiveLayoutTestTags {
    const val TWO_PANE_LAYOUT = "two_pane_layout"
    const val LIST_PANE = "list_pane"
    const val DETAIL_PANE = "detail_pane"
    const val PLACEHOLDER_PANE = "placeholder_pane"
    const val PANE_DIVIDER = "pane_divider"
}

/**
 * A two-pane layout component for expanded width displays.
 *
 * This composable arranges a list pane and a detail pane side by side,
 * suitable for tablets, foldables, and desktop displays.
 *
 * @param listPane The content for the list/master pane (left side)
 * @param detailPane The content for the detail pane (right side)
 * @param showDetailPane Whether the detail pane should be visible
 * @param listPaneWeight Weight for the list pane (default 0.4f = 40% width)
 * @param modifier Modifier for the root container
 */
@Composable
fun TwoPaneLayout(
    listPane: @Composable () -> Unit,
    detailPane: @Composable () -> Unit,
    showDetailPane: Boolean,
    modifier: Modifier = Modifier,
    listPaneWeight: Float = 0.5f,
    dividerWidth: Dp = 1.dp,
    placeholder: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .testTag(AdaptiveLayoutTestTags.TWO_PANE_LAYOUT)
            .semantics {
                contentDescription = "Two pane layout with list and detail"
            }
    ) {
        // List Pane
        Box(
            modifier = Modifier
                .weight(listPaneWeight)
                .fillMaxHeight()
                .testTag(AdaptiveLayoutTestTags.LIST_PANE)
                .semantics {
                    paneTitle = "List pane"
                }
        ) {
            listPane()
        }

        // Divider
        VerticalDivider(
            modifier = Modifier
                .width(dividerWidth)
                .fillMaxHeight()
                .testTag(AdaptiveLayoutTestTags.PANE_DIVIDER),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Detail Pane
        Box(
            modifier = Modifier
                .weight(1f - listPaneWeight)
                .fillMaxHeight()
                .testTag(AdaptiveLayoutTestTags.DETAIL_PANE)
                .semantics {
                    paneTitle = "Detail pane"
                }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = showDetailPane,
                    enter = fadeIn() + slideInHorizontally { it / 4 },
                    exit = fadeOut() + slideOutHorizontally { it / 4 }
                ) {
                    detailPane()
                }

                AnimatedVisibility(
                    visible = !showDetailPane,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(AdaptiveLayoutTestTags.PLACEHOLDER_PANE)
                    ) {
                        placeholder?.invoke()
                    }
                }
            }
        }
    }
}

/**
 * Configuration for two-pane layouts.
 */
@Stable
data class TwoPaneConfig(
    val listPaneWeight: Float = 0.4f,
    val dividerWidth: Dp = 1.dp,
    val animatePaneTransitions: Boolean = true
)

