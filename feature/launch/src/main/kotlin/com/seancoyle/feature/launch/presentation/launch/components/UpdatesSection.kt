package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUpdateUI

@Composable
internal fun UpdatesSection(
    updates: List<LaunchUpdateUI>,
    modifier: Modifier = Modifier,
    onSectionExpand: () -> Unit = {}
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    val visibleUpdates = updates.take(5)
    val collapsibleUpdates = updates.drop(5)
    val hasMoreUpdates = collapsibleUpdates.isNotEmpty()

    val expandedStateDesc = if (isExpanded) {
        stringResource(R.string.a11y_expanded)
    } else {
        stringResource(R.string.a11y_collapsed)
    }

    AppCard.Primary(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (hasMoreUpdates) {
                        Modifier
                            .clickable(role = Role.Button) {
                                if (!isExpanded) {
                                    onSectionExpand()
                                }
                                isExpanded = !isExpanded
                            }
                            .semantics {
                                role = Role.Button
                                stateDescription = expandedStateDesc
                            }
                    } else {
                        Modifier
                    }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(text = stringResource(R.string.launch_updates))

            if (hasMoreUpdates) {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null, // State is announced via semantics
                    modifier = Modifier.rotate(rotationAngle),
                    tint = AppTheme.colors.onSurface
                )
            }
        }

        // Always show first 5 updates
        visibleUpdates.forEach { update ->
            UpdateItem(update = update)
        }

        // Show remaining updates when expanded
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingLarge)) {
                collapsibleUpdates.forEach { update ->
                    UpdateItem(update = update)
                }
            }
        }
    }
}

@Composable
private fun UpdateItem(
    update: LaunchUpdateUI,
    modifier: Modifier = Modifier
) {
    AppCard.Tinted(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            AppText.labelMedium(
                text = update.createdBy,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.primary
            )
            AppText.labelSmall(
                text = update.createdOn,
                color = AppTheme.colors.onSurfaceVariant
            )
        }

        AppText.bodyMedium(
            text = update.comment,
            color = AppTheme.colors.onSurface
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun UpdatesSectionPreview() {
    AppTheme {
        UpdatesSection(
            updates = previewData().updates
        )
    }
}
