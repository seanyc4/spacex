package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.domain.model.LaunchUpdate
import com.seancoyle.feature.launch.domain.model.MissionPatch

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

@Composable
internal fun MissionPatchesSection(
    patches: List<MissionPatch>,
    modifier: Modifier = Modifier
) {
    SectionCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.dp16)) {
            SectionTitle(text = "Mission Patches")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.dp16)
            ) {
                patches.forEach { patch ->
                    MissionPatchItem(patch = patch, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MissionPatchItem(
    patch: MissionPatch,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.dp8)
    ) {
        GlideImage(
            model = patch.imageUrl,
            contentDescription = "Mission patch: ${patch.name}",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        AppText.labelSmall(
            text = patch.name ?: "Patch",
            color = AppTheme.colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun UpdatesSectionPreview() {
    AppTheme {
        UpdatesSection(
            updates = listOf(
                LaunchUpdate(
                    id = 1,
                    profileImage = null,
                    comment = "Launch scrubbed due to weather conditions. New T-0 is in 24 hours.",
                    infoUrl = null,
                    createdBy = "SpaceX",
                    createdOn = "Jan 14, 2026 6:00 PM"
                ),
                LaunchUpdate(
                    id = 2,
                    profileImage = null,
                    comment = "All systems are go for launch tomorrow.",
                    infoUrl = null,
                    createdBy = "Launch Director",
                    createdOn = "Jan 15, 2026 8:00 AM"
                )
            )
        )
    }
}
