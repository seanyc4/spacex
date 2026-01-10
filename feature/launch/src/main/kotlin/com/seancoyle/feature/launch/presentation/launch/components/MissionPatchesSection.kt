package com.seancoyle.feature.launch.presentation.launch.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.components.image.RemoteImage
import com.seancoyle.core.ui.designsystem.card.AppCard
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.core.ui.designsystem.theme.Dimens.horizontalArrangementSpacingLarge
import com.seancoyle.core.ui.designsystem.theme.Dimens.verticalArrangementSpacingSmall
import com.seancoyle.core.ui.designsystem.theme.PreviewDarkLightMode
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launch.model.MissionPatchUI

@Composable
internal fun MissionPatchesSection(
    patches: List<MissionPatchUI>,
    modifier: Modifier = Modifier
) {
    AppCard.Primary(modifier = modifier) {
        SectionTitle(text = stringResource(R.string.mission_patches))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangementSpacingLarge)
        ) {
            patches.forEach { patch ->
                MissionPatchItem(patch = patch, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MissionPatchItem(
    patch: MissionPatchUI,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(verticalArrangementSpacingSmall)
    ) {
        RemoteImage(
            imageUrl = patch.imageUrl,
            contentDescription = stringResource(R.string.mission_patch, patch.name),
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )
        AppText.labelSmall(
            text = patch.name,
            color = AppTheme.colors.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@PreviewDarkLightMode
@Composable
private fun MissionPatchesSectionPreview() {
    AppTheme {
        MissionPatchesSection(
            patches = previewData().missionPatches
        )
    }
}
