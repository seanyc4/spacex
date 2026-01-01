package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.presentation.launch.previewData

@Composable
internal fun UpdatesSection(
    updates: List<LaunchUpdate>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Launch Updates")

            updates.forEach { update ->
                UpdateItem(update = update)
            }
        }
    }
}

@Composable
private fun UpdateItem(
    update: LaunchUpdate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Dimens.dp12)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.dp16),
            verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
        ) {
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
