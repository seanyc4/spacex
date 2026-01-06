package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.domain.model.LaunchUpdate

@Composable
internal fun UpdatesSection(
    updates: List<LaunchUpdate>,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.launch_updates))

        updates.forEach { update ->
            UpdateItem(update = update)
        }
    }
}

@Composable
private fun UpdateItem(
    update: LaunchUpdate,
    modifier: Modifier = Modifier
) {
    AppCard.Subtle(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText.labelMedium(
                text = update.createdBy ?: "Unknown",
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.primary
            )
            AppText.labelSmall(
                text = update.createdOn ?: "",
                color = AppTheme.colors.onSurfaceVariant
            )
        }

        AppText.bodyMedium(
            text = update.comment ?: "",
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
